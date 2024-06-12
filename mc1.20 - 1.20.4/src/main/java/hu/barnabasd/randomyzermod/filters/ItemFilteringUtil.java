package hu.barnabasd.randomyzermod.filters;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemFilteringUtil {

    //
    // TODO implement reset, list
    //

    @Contract(pure = true)
    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> GenerateAllowListCommand() {
        LiteralArgumentBuilder<CommandSourceStack> allowListCommand = Commands.literal("allowList");
        LiteralArgumentBuilder<CommandSourceStack> allowListRemover = Commands.literal("remove");
        LiteralArgumentBuilder<CommandSourceStack> allowListAdder = Commands.literal("add");

        ItemFiltering.allowedItems.forEach(allowedItem -> {
            allowListRemover.then(Commands.literal(allowedItem)
                .executes(c -> ItemFiltering.AddToAllowList(c, allowedItem)));
            allowListAdder.then(Commands.literal(allowedItem)
                .executes(c -> ItemFiltering.RmFromAllowList(c, allowedItem)));
        });

        return allowListCommand;
    }

    @Contract(pure = true)
    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> GenerateDenyListCommand() {
        LiteralArgumentBuilder<CommandSourceStack> denyListCommand = Commands.literal("denyList");
        LiteralArgumentBuilder<CommandSourceStack> denyListRemover = Commands.literal("remove");
        LiteralArgumentBuilder<CommandSourceStack> denyListAdder = Commands.literal("add");

        ItemFiltering.deniedItems.forEach(deniedItem -> {
            denyListRemover.then(Commands.literal(deniedItem)
                .executes(c -> ItemFiltering.AddToDenyList(c, deniedItem)));
            denyListAdder.then(Commands.literal(deniedItem)
                .executes(c -> ItemFiltering.RmFromDenyList(c, deniedItem)));
        });

        return denyListCommand;
    }

}
