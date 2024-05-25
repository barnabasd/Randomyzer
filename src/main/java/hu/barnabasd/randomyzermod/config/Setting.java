package hu.barnabasd.randomyzermod.config;

import hu.barnabasd.randomyzermod.MainMod;
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

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        Value = (T)value;
        if (OnValueUpdate != null)
            OnValueUpdate.call(Value);
    }

    public void resetValue() {
        Value = Reset;
    }

    public interface Callback<T> {
        void call(T newVal);
    }

}
