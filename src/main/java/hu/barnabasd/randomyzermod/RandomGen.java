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

    public static @NotNull Item GetItem() {
        int itemIndex = (int)(Items.size() * Math.random());
        return (Item) Items.toArray()[itemIndex];
    }

    public static void RunCycle(MinecraftServer server) {
        List<ServerPlayer> players = PlayerFiltering.GetFilteredPlayers(server);
        ProjectStrings.DistributionType type = (ProjectStrings.DistributionType) Setting.ByName(ProjectStrings.GiveTypeId).getValue();
        int itemCount = (int) Setting.ByName(ProjectStrings.ItemCountId).getValue();

        if (type == ProjectStrings.DistributionType.randomMultipleItems) {
            for (ServerPlayer player : players)
                for (int i = 0; i < itemCount; i++)
                    player.getInventory().add(new ItemStack(GetItem(), 1));
        } else if (type == ProjectStrings.DistributionType.randomSameItem) {
            for (ServerPlayer player : players)
                player.getInventory().add(new ItemStack(GetItem()));
        } else if (type == ProjectStrings.DistributionType.sameMultipleItems) {
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                items.add(GetItem());
            }
            for (Item item : items)
                players.forEach(x -> x.getInventory().add(new ItemStack(item)));
        } else if (type == ProjectStrings.DistributionType.sameSameItem) {
            Item item = GetItem();
            for (ServerPlayer player : players) {
                player.getInventory().add(new ItemStack(item, itemCount));
            }
        } else {
            for (ServerPlayer player : players)
                player.sendSystemMessage(Component.literal("An internal error occurred when trying to give items."));
        }
    }

}
