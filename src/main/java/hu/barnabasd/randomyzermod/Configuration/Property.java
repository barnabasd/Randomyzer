package hu.barnabasd.randomyzermod.Configuration;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Property<T> {

    public Property(@NotNull String name, String description, String identifier, T reset) {
        Id = identifier; Name = name; Value = Reset = reset; Description = description;
    }

    public static final Map<String, Property<?>> Configuration = Map.of(
        "0", new Property<>("Nah", "PropertyDescription", "0", true),
        "1", new Property<>("Yes", "PropertyDescription", "1", 10)
    );

    public final String Name, Id, Description;
    public T Value, Reset;

    public @NotNull Component GetButtons() {
        if (Value instanceof Integer) return Component.empty()
            .append(Component.literal("§4[-5] ").setStyle(Style.EMPTY.withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer " + Id + " " + "-5"))))
            .append(Component.literal("§4[-1] ").setStyle(Style.EMPTY.withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer " + Id + " " + "-1"))))
            .append(Component.literal("§6§l[R] ").setStyle(Style.EMPTY.withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer " + Id + " " + Reset))))
            .append(Component.literal("§2[+1] ").setStyle(Style.EMPTY.withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer " + Id + " " + "+1"))))
            .append(Component.literal("§2[+5]").setStyle(Style.EMPTY.withClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer " + Id + " " + "+5"))));
        if (Value instanceof Boolean) return Component.literal("BooleanEngine");
        else return Component.literal("No.");
    }

}
