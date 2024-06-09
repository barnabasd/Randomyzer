package hu.barnabasd.randomyzermod.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.ProjectStrings;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"SameReturnValue", "unchecked"})
public class Configuration {

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(ProjectStrings.Config);
        for (Setting<?, ?> Option : ProjectStrings.Options) {
            LiteralArgumentBuilder<CommandSourceStack> propertyCommand = Commands.literal(Option.getName()).executes(c -> GetProperty(c, Option));
            LiteralArgumentBuilder<CommandSourceStack> set = Commands.literal("set");
            if (Option.getValue() instanceof Integer)
                set.then(Commands.argument("value", IntegerArgumentType.integer(1, (int)Option.getMaxValue())).executes(c -> SetIntProperty(c, Option)));
            else if (Option.getValue() instanceof Boolean)
                set.then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> SetBoolProperty(c, Option)));
            else if (Option.getValue() instanceof Enum<?>) {
                Setting<Enum<?>, Enum<?>[]> enumSetting = (Setting<Enum<?>, Enum<?>[]>)Option;
                for (Enum<?> property : enumSetting.getMaxValue())
                    set.then(Commands.literal(property.name()).executes(c -> SetParameterProperty(c, enumSetting, property)));
            }
            propertyCommand.then(set);
            propertyCommand.then(Commands.literal("reset").executes(c -> ResetProperty(c, Option)));
            command.then(propertyCommand);
        }
        return command;
    }

    private static int SetParameterProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Setting<Enum<?>, Enum<?>[]> option, @NotNull Enum<?> value) {
        option.setValue(value, c);
        if (c.getSource().getPlayer() != null)
            Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int SetBoolProperty(CommandContext<CommandSourceStack> c, @NotNull Setting<?, ?> option) {
        if (!(option.getValue() instanceof Boolean)) return 0;
        Setting<Boolean, ?> setting = (Setting<Boolean, ?>)option;
        setting.setValue(BoolArgumentType.getBool(c, "value"), c);
        if (c.getSource().getPlayer() != null)
            Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int SetIntProperty(CommandContext<CommandSourceStack> c, @NotNull Setting<?, ?> option) {
        if (!(option.getValue() instanceof Integer)) return 0;
        Setting<Integer, ?> setting = (Setting<Integer, ?>)option;
        setting.setValue(IntegerArgumentType.getInteger(c, "value"), c);
        if (c.getSource().getPlayer() != null)
            Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }

    private static int ResetProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Setting<?, ?> option) {
        option.resetValue();
        if (c.getSource().getPlayer() != null) Messages.SendReset(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int GetProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Setting<?, ?> option) {
        if (c.getSource().getPlayer() != null) Messages.SendGet(c.getSource().getPlayer(), option);
        return 1;
    }

}