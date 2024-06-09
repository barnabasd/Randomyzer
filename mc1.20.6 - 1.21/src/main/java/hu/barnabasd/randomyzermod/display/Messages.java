package hu.barnabasd.randomyzermod.display;

import hu.barnabasd.randomyzermod.RandomGen;
import hu.barnabasd.randomyzermod.config.Setting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Messages {

    public static void SendSet(@NotNull ServerPlayer player, @NotNull Setting<?, ?> setting) {
        player.sendSystemMessage(Component.literal("§6" + setting.getName() +
            "§r has been successfully set to: §6" + setting.getValue() + "§r."));
    }
    public static void SendReset(@NotNull ServerPlayer player, @NotNull Setting<?, ?> setting) {
        player.sendSystemMessage(Component.literal("§6" + setting.getName() +
            "§r has been successfully reset to: §6" + setting.getValue() + "§r."));
    }
    public static void SendGet(@NotNull ServerPlayer player, @NotNull Setting<?, ?> setting) {
        player.sendSystemMessage(Component.literal("§6" + setting.getName() +
            "§r is: §6" + setting.getValue() + "§r."));
    }

    public static void SendEmptyPlayerFilters(@NotNull ServerPlayer player) {
        player.sendSystemMessage(Component.literal("There are no players that get items."));
    }
    public static void SendPlayerFilters(@NotNull ServerPlayer player, @NotNull List<ServerPlayer> filter) {
        Component msg = Component.empty(); for (Component x : filter.stream().map(Player::getName).toList())
            msg = msg.plainCopy().append(x); player.sendSystemMessage(msg);
    }

    public static void SendEmptyItemFilters(@NotNull ServerPlayer player) {
        player.sendSystemMessage(Component.literal("There are no mods enabled to supply items."));
    }
    public static void SendItemFilters(@NotNull ServerPlayer player, @NotNull List<String> filter) {
        StringBuilder msg = new StringBuilder(); for (String x : filter)
            msg.append(x); player.sendSystemMessage(Component.literal(msg.toString()));
    }
}
