package hu.barnabasd.randomyzermod.config;

import hu.barnabasd.randomyzermod.MainMod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Setting<T> {
    public final Callback OnValueUpdate;
    public final String Name;
    public final T Reset;
    public T Value;

    public Setting(String name, T value, Callback onValueUpdate) {
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

    public void setValue(Object value) {
        Value = (T) value;
        if (OnValueUpdate != null)
            OnValueUpdate.call(value);
    }

    public void resetValue() {
        Value = Reset;
    }

    public interface Callback {
        void call(Object newVal);
    }

}
