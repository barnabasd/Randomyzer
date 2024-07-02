package hu.barnabasd.randomyzer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ItemAlgorithms {

    public static Item GetRandomItem() {
        List<Item> ITEMS = ItemFiltering.GetApplicableItems();
        int index = new Random().nextInt(0, ITEMS.size());
        return ITEMS.get(index);
    }

    public static void Execute(List<ServerPlayer> players) {
        RandomyzerCommand.DistributionType type = RandomyzerCommand.distributionType.GetValue();
        int count = RandomyzerCommand.itemCount.GetValue();
        if (type == RandomyzerCommand.DistributionType.randomDifferent)
            for (ServerPlayer player : players)
                for (int i = 0; i < count; i++)
                    player.getInventory().add(new ItemStack(GetRandomItem()));

        if (type == RandomyzerCommand.DistributionType.sameDifferent)
            for (ServerPlayer player : players)
                player.getInventory().add(new ItemStack(GetRandomItem(), count));

        if (type == RandomyzerCommand.DistributionType.randomGlobal) {
            List<Item> items = IntStream.range(0, count).mapToObj(i -> GetRandomItem()).toList();
            for (ServerPlayer player : players)
                for (Item i : items)
                    player.getInventory().add(new ItemStack(i));
        }

        if (type == RandomyzerCommand.DistributionType.sameGlobal) {
            Item item = GetRandomItem();
            for (ServerPlayer player : players)
                player.getInventory().add(new ItemStack(item, count));
        }
    }

}
