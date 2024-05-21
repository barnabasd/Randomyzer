package hu.barnabasd.randomyzermod.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.MainMod;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("SameReturnValue")
public class Configuration {

    public static void CreateCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        for (Setting<?> Option : MainMod.Options) {
            LiteralArgumentBuilder<CommandSourceStack> propertyCommand =
                Commands.literal(Option.Name).executes(c -> GetProperty(c,  Option));
            LiteralArgumentBuilder<CommandSourceStack> set = Commands.literal("set");
            if (Option.Value instanceof Integer)
                set.then(Commands.argument("value", IntegerArgumentType.integer()).executes(c -> SetIntProperty(c, Option)));
            else if (Option.Value instanceof Boolean)
                set.then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> SetBoolProperty(c, Option)));
            else if (Option instanceof SelectionSetting<?,?> propertyData) {
                for (Enum<?> property : ((Enum<?>[])propertyData.AvailibleOptions))
                    set.then(Commands.literal(property.name()).executes(c -> SetParameterProperty(c, propertyData)));
            }
            propertyCommand.then(set);
            propertyCommand.then(Commands.literal("reset").executes(c -> ResetProperty(c, Option)));
            command.then(propertyCommand);
        }
    }

    private static int SetParameterProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull SelectionSetting<?, ?> option) {
        String newProperty = c.getInput().split(" ")[c.getInput().split(" ").length - 1];
        Enum<?> newActualProperty = Arrays.stream(((Enum<?>[])option.AvailibleOptions))
            .filter(x -> x.name().equals(newProperty)).findAny().get();
        option.setValue(newActualProperty);
        if (c.getSource().getPlayer() != null) Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int SetBoolProperty(CommandContext<CommandSourceStack> c, @NotNull Setting<?> option) {
        if (!(option.Value instanceof Boolean)) return 0;
        Setting<Boolean> setting = (Setting<Boolean>)option;
        setting.setValue(BoolArgumentType.getBool(c, "value"));
        if (c.getSource().getPlayer() != null) Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int SetIntProperty(CommandContext<CommandSourceStack> c, @NotNull Setting<?> option) {
        if (!(option.Value instanceof Integer)) return 0;
        Setting<Integer> setting = (Setting<Integer>)option;
        setting.setValue(IntegerArgumentType.getInteger(c, "value"));
        if (c.getSource().getPlayer() != null) Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }

    private static int ResetProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Setting<?> option) {
        option.resetValue();
        if (c.getSource().getPlayer() != null) Messages.SendReset(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int GetProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Setting<?> option) {
        if (c.getSource().getPlayer() != null) Messages.SendGet(c.getSource().getPlayer(), option);
        return 1;
    }

}