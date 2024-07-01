package hu.barnabasd.randomyzer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.NotNull;

public class Countdown {

    private static final ServerBossEvent bossbar = new ServerBossEvent(
        Component.literal(" seconds until next item...")
            .withStyle(style -> style.withColor(ChatFormatting.YELLOW)),
        BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS
    );

    public static void HideAll(@NotNull MinecraftServer server) {
        bossbar.getPlayers().clear();
        server.getPlayerList().getPlayers().forEach(player -> {
            player.sendSystemMessage(Component.empty());
        });
    }

    public static void Execute(MinecraftServer server) {

    }

}
