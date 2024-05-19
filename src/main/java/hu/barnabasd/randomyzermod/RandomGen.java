package hu.barnabasd.randomyzermod;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.item.Item;

import java.util.Collection;
import java.util.List;

public class RandomGen {
    public enum GenType { random_individual, uniform_individual, random_shared, uniform_shared }

    public static Collection<Item> Items = ForgeRegistries.ITEMS.getValues();

    public static @NotNull ItemStack GetItem(int count) {
        int itemIndex = (int)(Items.size() * Math.random());
        Item selectedItem = (Item)Items.toArray()[itemIndex];
        return new ItemStack(selectedItem, count);
    }

    public static void RunCycle(List<ServerPlayer> players) {
        GenType type = (GenType)MainMod.Options.get(1).Value;
        int itemCount = (int)MainMod.Options.get(2).Value;

        if (type == GenType.random_individual) {
            for (ServerPlayer player : players)
                for (int i = 0; i < itemCount; i++)
                    player.getInventory().add(GetItem(1));
        }
        else if (type == GenType.uniform_individual) {
            for (ServerPlayer player : players)
                player.getInventory().add(GetItem(itemCount));
        }
        else if (type == GenType.random_shared) {
            ItemStack[] items = new ItemStack[itemCount];
            for (int i = 0; i < itemCount; i++)
                items[i] = GetItem(1);
            for (ServerPlayer player : players)
                for (ItemStack item : items)
                    player.getInventory().add(item);
        }
        else if (type == GenType.uniform_shared) {
            ItemStack item = GetItem(itemCount);
            for (ServerPlayer player : players)
                player.getInventory().add(item);
        }
        else {
            for (ServerPlayer player : players)
                player.sendSystemMessage(Component.literal("An internal error occoured when trying to give items."));
        }
    }

}
