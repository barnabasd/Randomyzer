package hu.barnabasd.randomyzer;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

public abstract class CustomGameRule<T> {
    public interface ChangedEvent<T> { void call(MinecraftServer server, T newVal); }

    public final ChangedEvent<T> onChanged;
    public final String name;
    private T value;

    public CustomGameRule(String name, T defaultValue)
    { this.name = name; this.value = defaultValue; this.onChanged = (e, f) -> {}; }
    public CustomGameRule(String name, T defaultValue, ChangedEvent<T> changed)
    { this.name = name; this.value = defaultValue; this.onChanged = changed; }

    public LiteralArgumentBuilder<CommandSourceStack> GetCommand() {
        return Commands.literal(name);
    }

    public T GetValue() { return this.value; }

    public void SendValueToPlayer(@Nullable ServerPlayer player) { SendValueToPlayer(player, false); }
    public void SendValueToPlayer(@Nullable ServerPlayer player, boolean isSet) {
        if (player == null) return; String action = isSet ? "now" : "currently";
        player.sendSystemMessage(Component.literal(
            "Setting " + this.name + " is " + action + " set to: " + this.value.toString()));
    }

    public void SetValue(T value, @NotNull CommandContext<CommandSourceStack> c) {
        this.value = value;
        SendValueToPlayer(c.getSource().getPlayer(), true);
        onChanged.call(c.getSource().getServer(), value);
    }

    public static class BooleanRule extends CustomGameRule<Boolean> {
        public BooleanRule(String name, boolean defaultValue)
            { super(name, defaultValue); }
        public BooleanRule(String name, boolean defaultValue, ChangedEvent<Boolean> changed)
            { super(name, defaultValue, changed); }
        @Override public LiteralArgumentBuilder<CommandSourceStack> GetCommand() {
            return Commands.literal(this.name).executes(c -> {
                    SendValueToPlayer(c.getSource().getPlayer()); return 1;
                })
                .then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> {
                    SetValue(BoolArgumentType.getBool(c, "value"), c); return 1;
                }));
        }
    }
    public static class IntegerRule extends CustomGameRule<Integer> {
        private final int MIN, MAX;
        public IntegerRule(String name, int min, int max, int defaultValue)
            { super(name, defaultValue); MIN = min; MAX = max; }
        public IntegerRule(String name, int min, int max, int defaultValue, ChangedEvent<Integer> changed)
            { super(name, defaultValue, changed); MIN = min; MAX = max; }
        @Override public LiteralArgumentBuilder<CommandSourceStack> GetCommand() {
            return Commands.literal(this.name).executes(c -> {
                    SendValueToPlayer(c.getSource().getPlayer()); return 1;
                })
                .then(Commands.argument("value", IntegerArgumentType.integer(MIN, MAX)).executes(c -> {
                    SetValue(IntegerArgumentType.getInteger(c, "value"), c); return 1;
                }));
        }
    }
    public static class EnumRule<T extends Enum<?>> extends CustomGameRule<T> {
        private final T[] availableValues;
        public EnumRule(String name, T[] values, T defaultValue)
            { super(name, defaultValue); availableValues = values; }
        public EnumRule(String name, T[] values, T defaultValue, ChangedEvent<T> changed)
            { super(name, defaultValue, changed); availableValues = values; }
        @Override public LiteralArgumentBuilder<CommandSourceStack> GetCommand() {
            LiteralArgumentBuilder<CommandSourceStack> mainCommand = Commands.literal(this.name).executes(c -> {
                SendValueToPlayer(c.getSource().getPlayer()); return 1;
            });
            Arrays.stream(availableValues).forEach(option -> {
                mainCommand.then(Commands.literal(option.name()).executes(c -> {
                    SetValue(option, c); return 1;
                }));
            });
            return mainCommand;
        }
    }

}
