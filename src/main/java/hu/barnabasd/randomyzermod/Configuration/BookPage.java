package hu.barnabasd.randomyzermod.Configuration;

import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

// Si le code s'exécute la première fois, la fonction n'a pas été appelée

public class BookPage {

    private static @NotNull String Center(@NotNull String data) {
        return "-".repeat(12 - data.length()) + " " + data + " ";
    }

    public static @NotNull StringTag FromProperty(@NotNull Property<?> Property) {
        MutableComponent page = Component.empty();

        page.append(Component.literal("§4[BACK] ").withStyle(Style.EMPTY.withClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer"))));
        page.append(Center(Property.Name));
        page.append("\nCurrent value: " + Property.Value);
        page.append("\nDefault value: " + Property.Reset);
        page.append("\n\n");
        page.append(Property.GetButtons());
        page.append("\n\n" + Property.Description);

        return StringTag.valueOf(Component.Serializer.toJson(page));
    }

    public static @NotNull StringTag FromProperties() {
        MutableComponent page = Component.empty();

        page.append(Component.literal("§4[BACK] ").withStyle(Style.EMPTY.withClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer"))));
        page.append(Center("Conf"));
        page.append("\n\n");
        for (Property<?> Property : Property.Configuration.values()) {
            page.append(" - ");
            page.append(Component.literal("§n" + Property.Name).withStyle(
                Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/randomyzer " + Property.Id))));
            page.append("\n");
        }

        return StringTag.valueOf(Component.Serializer.toJson(page));
    }

}