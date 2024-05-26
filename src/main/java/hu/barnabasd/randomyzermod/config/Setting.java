package hu.barnabasd.randomyzermod.config;

import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.ConfigOptions;
import net.minecraft.commands.CommandSourceStack;

import java.util.Objects;

public class Setting<T, Q> {
    private final String name;
    private T value;
    private final T resetValue;
    private final Q maxValue;
    private Callback<T> callback = null;

    public Setting(String name, T value, Q maxValue) {
        this.name = name;
        this.value = this.resetValue = value;
        if (maxValue instanceof Integer || maxValue instanceof Enum[]) {
            this.maxValue = maxValue;
        } else {
            throw new IllegalArgumentException("maxValue must be either Integer or Enum values.");
        }
    }
    public Setting(String name, T value, Q maxValue, Callback<T> callback) {
        this(name, value, maxValue);
        this.callback = callback;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value, CommandContext<CommandSourceStack> c) {
        this.value = value;
        if (callback != null) {
            callback.call(c, value);
        }
    }

    public void resetValue() {
        value = resetValue;
    }

    public Q getMaxValue() {
        return maxValue;
    }

    public interface Callback<T> {
        void call(CommandContext<CommandSourceStack> c, T newValue);
    }

    public static Setting<?, ?> ByName(String name) {
        return ConfigOptions.Options.stream().filter(x -> Objects.equals(x.getName(), name)).findAny().orElse(new Setting<>("null", null, 0));
    }
}
