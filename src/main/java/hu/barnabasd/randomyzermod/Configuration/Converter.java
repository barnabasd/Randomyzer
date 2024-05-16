package hu.barnabasd.randomyzermod.Configuration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Converter {

    public static @NotNull ItemStack createBook(List<StringTag> pages) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag bookTag = new CompoundTag();
        bookTag.putString("title", "Igen");
        bookTag.putString("author", "Nem");
        ListTag listTag = new ListTag();
        listTag.addAll(pages);
        bookTag.put("pages", listTag);
        book.setTag(bookTag);
        return book;
    }
    public static void OpenBook(StringTag page, @NotNull ServerPlayer player)
        { OpenBook(Collections.singletonList(page), player); }
    public static void OpenBook(List<StringTag> pages, @NotNull ServerPlayer player) {
        ItemStack originalItem = player.getInventory().getItem(player.getInventory().selected);
        player.getInventory().setItem(player.getInventory().selected, createBook(pages));
        player.openItemGui(createBook(pages), InteractionHand.MAIN_HAND);
        player.getInventory().setItem(player.getInventory().selected, originalItem);
    }

}
