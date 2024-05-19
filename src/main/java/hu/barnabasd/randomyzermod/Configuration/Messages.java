package hu.barnabasd.randomyzermod.Configuration;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class Messages {

    public static void SendSet(@NotNull ServerPlayer player, ConfCommand.@NotNull Property<?> property) {
        player.sendSystemMessage(Component.literal("§6" + property.Name +
            "§r has been successfully set to: §6" + property.Value + "§r."));
    }

    public static void SendReset(@NotNull ServerPlayer player, ConfCommand.@NotNull Property<?> property) {
        player.sendSystemMessage(Component.literal("§6" + property.Name +
            "§r has been successfully reset to: §6" + property.Value + "§r."));
    }

    public static void SendValue(@NotNull ServerPlayer player, ConfCommand.@NotNull Property<?> property) {
        player.sendSystemMessage(Component.literal("§6" + property.Name +
            "§r is: §6" + property.Value + "§r."));
    }
}
