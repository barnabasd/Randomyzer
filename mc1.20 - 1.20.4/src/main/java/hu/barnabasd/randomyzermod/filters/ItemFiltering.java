package hu.barnabasd.randomyzermod.filters;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.RandomGen;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemFiltering {

    public static List<String> allModIds = new ArrayList<>();
    private static List<String> enabledModIds = allModIds;

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("items");
        LiteralArgumentBuilder<CommandSourceStack> addPart = Commands.literal("add");
        allModIds.forEach(mod -> addPart.then(Commands.literal(mod).executes(c -> AddMod(c, mod))));
        LiteralArgumentBuilder<CommandSourceStack> rmPart = Commands.literal("remove");
        allModIds.forEach(mod -> addPart.then(Commands.literal(mod).executes(c -> RmMod(c, mod))));
        command.then(addPart).then(rmPart)
            .then(Commands.literal("reset").executes(ItemFiltering::Reset))
            .then(Commands.literal("list").executes(ItemFiltering::ListMods)
        );
        return command;
    }

    private static int ListMods(CommandContext<CommandSourceStack> context) {
        if (enabledModIds.isEmpty())
            Messages.SendEmptyItemFilters(Objects.requireNonNull(context.getSource().getPlayer()));
        else
            Messages.SendItemFilters(Objects.requireNonNull(context.getSource().getPlayer()), enabledModIds);
        return 1;
    }

    private static int Reset(@NotNull CommandContext<CommandSourceStack> context) {
        enabledModIds = allModIds;
        Objects.requireNonNull(context.getSource().getPlayer())
            .sendSystemMessage(Component.literal("Successfully reset supplying mods."));
        return 1;
    }

    private static int RmMod(CommandContext<CommandSourceStack> context, String modId) {
        enabledModIds.remove(modId);
        RandomGen.Items = ForgeRegistries.ITEMS.getValues().stream().filter(
            x -> enabledModIds.contains(x.getCreatorModId(x.getDefaultInstance()))).toList();
        return 1;
    }

    private static int AddMod(CommandContext<CommandSourceStack> context, String modId) {
        enabledModIds.add(modId);
        RandomGen.Items = ForgeRegistries.ITEMS.getValues().stream().filter(
            x -> enabledModIds.contains(x.getCreatorModId(x.getDefaultInstance()))).toList();
        return 1;
    }

}
