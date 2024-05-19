package hu.barnabasd.randomyzermod;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.item.Item;

import java.util.Collection;

public class RandomGen {
    public enum GenType { random_individual, uniform_individual, random_shared, uniform_shared }

    public static Collection<Item> Items = ForgeRegistries.ITEMS.getValues();

    public static @NotNull ItemStack GetItem(int count) {
        int itemIndex = (int)(Items.size() * Math.random());
        Item selectedItem = (Item)Items.toArray()[itemIndex];
        return new ItemStack(selectedItem, count);
    }

}
