package hu.barnabasd.randomyzermod.filters;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemFilteringUtil {

    //
    // TODO implement list
    //

    @Contract(pure = true)
    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> GenerateAllowListCommand() {
        LiteralArgumentBuilder<CommandSourceStack> allowListCommand = Commands.literal("items");
        LiteralArgumentBuilder<CommandSourceStack> allowListRemover = Commands.literal("remove");
        LiteralArgumentBuilder<CommandSourceStack> allowListAppendr = Commands.literal("add");

        ItemFiltering.allowedItems.forEach(allowedItem -> {
            allowListRemover.then(Commands.literal(allowedItem.toString())
                .executes(c -> ItemFiltering.ApndToAllowList(c, allowedItem)));
            allowListAppendr.then(Commands.literal(allowedItem.toString())
                .executes(c -> ItemFiltering.RmFromAllowList(c, allowedItem)));
        });

        return allowListCommand.then(allowListAppendr).then(allowListRemover)
            .then(Commands.literal("reset").executes(ItemFiltering::ResetAllowList));
    }
}
