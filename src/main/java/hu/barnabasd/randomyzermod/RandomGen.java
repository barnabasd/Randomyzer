package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.config.Setting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RandomGen {
    public static final Collection<Item> Items = ForgeRegistries.ITEMS.getValues();

    public static @NotNull ItemStack GetItem(int count) {
        int itemIndex = (int) (Items.size() * Math.random());
        Item selectedItem = (Item) Items.toArray()[itemIndex];
        return new ItemStack(selectedItem, count);
    }

    public static void RunCycle(MinecraftServer server) {
        List<ServerPlayer> players = PlayerFiltering.GetFilteredPlayers(server);
        GenType type = (GenType) Setting.GetSettingByName("ItemDistributionMethod").Value;
        int itemCount = (int) Setting.GetSettingByName("ItemQuantity").Value;

        if (type == GenType.random_individual) {
            for (ServerPlayer player : players)
                for (int i = 0; i < itemCount; i++)
                    player.getInventory().add(GetItem(1));
        } else if (type == GenType.uniform_individual) {
            for (ServerPlayer player : players)
                player.getInventory().add(GetItem(itemCount));
        } else if (type == GenType.random_shared) {
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < itemCount; i++)
                items.add(GetItem(1));
            for (ServerPlayer player : players)
                for (ItemStack item : items)
                    player.getInventory().add(item);
        } else if (type == GenType.uniform_shared) {
            ItemStack item = GetItem(itemCount);
            for (ServerPlayer player : players)
                player.getInventory().add(item);
        } else {
            for (ServerPlayer player : players)
                player.sendSystemMessage(Component.literal("An internal error occurred when trying to give items."));
        }
    }

    public enum GenType {random_individual, uniform_individual, random_shared, uniform_shared}

}
