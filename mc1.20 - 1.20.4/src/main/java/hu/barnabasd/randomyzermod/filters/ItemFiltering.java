package hu.barnabasd.randomyzermod.filters;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemFiltering {

    public static List<Item> allowedItems = ItemFiltering.ALL_ITEMS;
    public static List<String> modIds = new ArrayList<>();

    public static final List<Item> ALL_ITEMS = ForgeRegistries.ITEMS.getValues().stream().toList();

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        return ItemFilteringUtil.GenerateAllowListCommand();
    }

    public static int ApndToAllowList(CommandContext<CommandSourceStack> c, Item item) {
        if (allowedItems.contains(item)) return 2;
        allowedItems.add(item);
        return 1;
    }
    public static int RmFromAllowList(CommandContext<CommandSourceStack> c, Item item) {
        if (!allowedItems.contains(item)) return 2;
        allowedItems.remove(item);
        return 1;
    }
    public static int ResetAllowList(CommandContext<CommandSourceStack> c) {
        allowedItems = ALL_ITEMS;
        return 1;
    }
    public static int ListAllowedItems(@NotNull CommandContext<CommandSourceStack> c) {
        Messages.SendItemFilters(Objects.requireNonNull(c.getSource().getPlayer()),
            allowedItems.stream().map(x -> x.getName(x.getDefaultInstance()).getString()).toList());
        Messages.SendReset(c.getSource().getPlayer(), new Setting<>("itemFilters", "", ""));
        return 1;
    }

}
