package hu.barnabasd.randomyzermod.config;

import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.MainMod;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Setting<T> {
    public final Callback<T> OnValueUpdate;
    public final String Name;
    public final T Reset;
    public T Value;

    public Setting(String name, T value, Callback<T> onValueUpdate) {
        Value = Reset = value; Name = name;
        OnValueUpdate = onValueUpdate;
    }
    public Setting(String name, T value) {
        Value = Reset = value;
        OnValueUpdate = null;
        Name = name;
    }

    public static @NotNull Setting<?> GetSettingByName(String name) {
        return MainMod.Options.stream().filter(x -> Objects.equals(x.Name, name))
            .findAny().orElse(new Setting<>("null", null));
    }

    @SuppressWarnings("unchecked") // Very bad code :D, but probably fine
    public void setValue(Object value, CommandContext<CommandSourceStack> c) {
        Object deferredValue = Integer.MIN_VALUE;
        if (OnValueUpdate != null)
            deferredValue = OnValueUpdate.call((T)value, Value, c);
        if (deferredValue == (Object)Integer.MIN_VALUE) {
            Value = (T)value;
        }
        else if (Value instanceof Integer) {
            Value = (T)deferredValue;
        }
    }

    public void resetValue() {
        Value = Reset;
    }

    public interface Callback<T> {
        int call(T newVal, T oldVal, CommandContext<CommandSourceStack> c);
    }

}
