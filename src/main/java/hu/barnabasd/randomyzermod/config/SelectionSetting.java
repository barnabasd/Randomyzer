package hu.barnabasd.randomyzermod.config;

public class SelectionSetting<T extends Enum<?>, Q> extends Setting<T> {
    public final Q AvailibleOptions;
    public SelectionSetting(String name, T value, Q availibleOptions) {
        super(name, value);
        AvailibleOptions = availibleOptions;
    }
}
