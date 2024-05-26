package hu.barnabasd.randomyzermod.display;

import hu.barnabasd.randomyzermod.config.Setting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

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
}
