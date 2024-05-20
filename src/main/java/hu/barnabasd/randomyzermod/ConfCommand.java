package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.UserInterface.Messages;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static hu.barnabasd.randomyzermod.MainMod.Options;

public class ConfCommand {

    public static class Property<T> {
        public interface Callback { void call(Object newVal); }
        public final T Reset; public T Value;
        public final Callback OnValueUpdate;
        public final String Name;
        public Property(String name, T value, Callback onValueUpdate) {
            Value = Reset = value; Name = name; OnValueUpdate = onValueUpdate;
        }
        public Property(String name, T value) {
            Value = Reset = value; Name = name; OnValueUpdate = null;
        }
        public void setValue(Object value) {
            Value = (T)value;
            if (OnValueUpdate != null)
                OnValueUpdate.call(value);
        }
        public void resetValue() { Value = Reset; }
    }
    public static class EnumProperty<T extends Enum<?>, Q> extends Property<T> {
        public final Q AvailibleOptions;
        public EnumProperty(String name, T value, Q availibleOptions) {
            super(name, value);
            AvailibleOptions = availibleOptions;
        }
        public EnumProperty(String name, T value, Q availibleOptions, Callback onUpdateValue) {
            super(name, value, onUpdateValue);
            AvailibleOptions = availibleOptions;
        }
    }
    public static void CreateCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        for (Property<?> Option : Options) {
            LiteralArgumentBuilder<CommandSourceStack> propertyCommand =
                Commands.literal(Option.Name).executes(c -> GetProperty(c,  Option));
            LiteralArgumentBuilder<CommandSourceStack> set = Commands.literal("set");
            if (Option.Value instanceof Integer)
                set.then(Commands.argument("value", IntegerArgumentType.integer()).executes(c -> SetIntProperty(c, Option)));
            else if (Option.Value instanceof Boolean)
                set.then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> SetBoolProperty(c, Option)));
            else if (Option instanceof EnumProperty<?,?> propertyData) {
                for (Enum<?> property : ((Enum<?>[])propertyData.AvailibleOptions))
                    set.then(Commands.literal(property.name()).executes(c -> SetParameterProperty(c, propertyData)));
            }
            propertyCommand.then(set);
            propertyCommand.then(Commands.literal("reset").executes(c -> ResetProperty(c, Option)));
            command.then(propertyCommand);
        }
    }

    private static int SetParameterProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull EnumProperty<?, ?> option) {
        String newProperty = c.getInput().split(" ")[c.getInput().split(" ").length - 1];
        Enum<?> newActualProperty = Arrays.stream(((Enum<?>[])option.AvailibleOptions))
            .filter(x -> x.name().equals(newProperty)).findAny().get();
        option.setValue(newActualProperty);
        if (c.getSource().getPlayer() != null) Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int SetBoolProperty(CommandContext<CommandSourceStack> c, @NotNull Property<?> option) {
        if (!(option.Value instanceof Boolean)) return 0;
        Property<Boolean> property = (Property<Boolean>)option;
        property.setValue(BoolArgumentType.getBool(c, "value"));
        if (c.getSource().getPlayer() != null) Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int SetIntProperty(CommandContext<CommandSourceStack> c, @NotNull Property<?> option) {
        if (!(option.Value instanceof Integer)) return 0;
        Property<Integer> property = (Property<Integer>)option;
        property.setValue(IntegerArgumentType.getInteger(c, "value"));
        if (c.getSource().getPlayer() != null) Messages.SendSet(c.getSource().getPlayer(), option);
        return 1;
    }

    private static int ResetProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Property<?> option) {
        option.resetValue();
        if (c.getSource().getPlayer() != null) Messages.SendReset(c.getSource().getPlayer(), option);
        return 1;
    }
    private static int GetProperty(@NotNull CommandContext<CommandSourceStack> c, @NotNull Property<?> option) {
        if (c.getSource().getPlayer() != null) Messages.SendGet(c.getSource().getPlayer(), option);
        return 1;
    }

}