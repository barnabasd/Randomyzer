package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.filters.PlayerFiltering;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RandomGen {
    public static Collection<Item> Items = ForgeRegistries.ITEMS.getValues();

    public static @NotNull Item GetItem() {
        int itemIndex = (int)(Items.size() * Math.random());
        return (Item) Items.toArray()[itemIndex];
    }

    public static void AddItemsToPlayerInv(@NotNull Item item, int count, ServerPlayer player) {
        int maxStack = item.getMaxStackSize();
        int stackCount = (count - (count % maxStack)) / maxStack;
        int lastStack = count % maxStack;
        for (int i = 0; i < stackCount; i++)
            player.getInventory().add(new ItemStack(item, maxStack));
        player.getInventory().add(new ItemStack(item, lastStack));
    }

    public static void RunCycle() {
        ProjectStrings.DistributionType type = (ProjectStrings.DistributionType) Setting.ByName(ProjectStrings.GiveTypeId).getValue();
        int itemCount = (int) Setting.ByName(ProjectStrings.ItemCountId).getValue();

        if (type == ProjectStrings.DistributionType.randomMultipleItems) {
            for (ServerPlayer player : PlayerFiltering.availablePlayers)
                for (int i = 0; i < itemCount; i++)
                    AddItemsToPlayerInv(GetItem(), 1, player);
        } else if (type == ProjectStrings.DistributionType.randomSameItem) {
            for (ServerPlayer player : PlayerFiltering.availablePlayers)
                AddItemsToPlayerInv(GetItem(), itemCount, player);
        } else if (type == ProjectStrings.DistributionType.sameMultipleItems) {
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < itemCount; i++) items.add(GetItem());
            for (Item item : items)
                PlayerFiltering.availablePlayers.forEach(x -> AddItemsToPlayerInv(item, 1, x));
        } else if (type == ProjectStrings.DistributionType.sameSameItem) {
            Item item = GetItem();
            for (ServerPlayer player : PlayerFiltering.availablePlayers)
                AddItemsToPlayerInv(item, itemCount, player);
        } else {
            for (ServerPlayer player : PlayerFiltering.availablePlayers)
                player.sendSystemMessage(Component.literal("An internal error occurred when trying to give items."));
        }
        CycleSounds.PlaySounds(PlayerFiltering.availablePlayers);
    }

}
