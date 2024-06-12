package hu.barnabasd.randomyzermod.filters;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemFiltering {

    public static List<String> allowedItems = ItemFiltering.ALL_ITEMS;
    public static List<String> deniedItems = new ArrayList<>();

    public static final List<String> ALL_ITEMS = ForgeRegistries.ITEMS.getValues()
            .stream().map(x -> x.getName(x.getDefaultInstance()).getString()).toList();

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        return Commands.literal("items")
            .then(ItemFilteringUtil.GenerateAllowListCommand())
            .then(ItemFilteringUtil.GenerateDenyListCommand());
    }

    public static int AddToAllowList(CommandContext<CommandSourceStack> c, String item) {
        return 1;
    }
    public static int RmFromAllowList(CommandContext<CommandSourceStack> c, String item) {
        return 1;
    }

    public static int AddToDenyList(CommandContext<CommandSourceStack> c, String item) {
        return 1;
    }
    public static int RmFromDenyList(CommandContext<CommandSourceStack> c, String item) {
        return 1;
    }

}
