package hu.barnabasd.randomyzer;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ItemFiltering {

    public static final List<Item> ALL_ITEMS = ForgeRegistries.ITEMS.getValues().stream().toList();

    public static List<Item> GetApplicableItems() {
        return ALL_ITEMS;
    }

}
