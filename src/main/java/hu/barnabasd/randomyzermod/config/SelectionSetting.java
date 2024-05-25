package hu.barnabasd.randomyzermod.config;

public class SelectionSetting<T extends Enum<?>, Q> extends Setting<T> {
    public final Q AvailableOptions;
    public SelectionSetting(String name, T value, Q availableOptions) {
        super(name, value);
        AvailableOptions = availableOptions;
    }
    public SelectionSetting(String name, T value, Q availableOptions, Callback<T> callback) {
        super(name, value, callback);
        AvailableOptions = availableOptions;
    }
}
