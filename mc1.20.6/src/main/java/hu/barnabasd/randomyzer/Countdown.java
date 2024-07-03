package hu.barnabasd.randomyzer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Countdown {

    public static final ServerBossEvent bossbar = new ServerBossEvent(
        Component.literal(String.valueOf((Randomyzer.CountDownTicks / 20)))
            .withStyle(style -> style.withColor(ChatFormatting.YELLOW)).append(
        Component.literal(" seconds until next item...").withStyle(
            style -> style.withColor(ChatFormatting.WHITE))),
        BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS
    );

    public static void HideAll(@NotNull MinecraftServer server) {
        bossbar.removeAllPlayers();
        server.getPlayerList().getPlayers().forEach(player ->
            player.sendSystemMessage(Component.empty(), true));
    }

    public static void Execute(@NotNull List<ServerPlayer> players) {
        if (RandomyzerCommand.countdownStyle.GetValue() == RandomyzerCommand.TimerDisplayType.bossbar) {
            players.forEach(bossbar::addPlayer); bossbar.setVisible(true);
            int maxTicks = RandomyzerCommand.countdownTime.GetValue() * 20;
            bossbar.setProgress((float)Randomyzer.CountDownTicks / maxTicks);
            bossbar.setName(Component.literal(String.valueOf((Randomyzer.CountDownTicks / 20)))
                .withStyle(style -> style.withColor(ChatFormatting.YELLOW)).append(
                    Component.literal(" seconds until next item...").withStyle(
                        style -> style.withColor(ChatFormatting.WHITE))));
        }

        if (RandomyzerCommand.countdownStyle.GetValue() == RandomyzerCommand.TimerDisplayType.actionbar) {
            players.forEach(p -> p.sendSystemMessage(
                Component.literal(String.valueOf((Randomyzer.CountDownTicks / 20)))
                    .withStyle(style -> style.withColor(ChatFormatting.YELLOW)).append(
                        Component.literal(" seconds until next item...").withStyle(
                            style -> style.withColor(ChatFormatting.WHITE))), true));
        }
    }

}
