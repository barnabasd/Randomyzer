package hu.barnabasd.randomyzermod.display;

import hu.barnabasd.randomyzermod.RandomGen;
import hu.barnabasd.randomyzermod.config.Setting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class CountdownDisplay {
    public static int CountDownTicks = (int)Setting.GetSettingByName("TimerDuration").Value * 20;
    public static boolean IsPaused = true;

    public enum DisplayStyle { bossbar, actionbar_text, actionbar_progressbar, expriencebar, hidden }

    public static void onServerTick(@NotNull TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || IsPaused) return;
        if (CountDownTicks > 1) CountDownTicks--;
        else {
            RandomGen.RunCycle(event.getServer());
            CountDownTicks = (int)Setting.GetSettingByName("TimerDuration").Value * 20;
        }
        if (Setting.GetSettingByName("TimerDsiplayMode").Value != DisplayStyle.hidden)
            DisplayCountdown(event.getServer());
        else {
            bossbar.removeAllPlayers();
            event.getServer().getPlayerList().getPlayers().forEach(x -> {
                x.setExperiencePoints(0);
                x.setExperienceLevels(0);
            });
        }
    }

    public static void DisplayCountdown(MinecraftServer server) {
        DisplayStyle style = (DisplayStyle)Setting.GetSettingByName("TimerDsiplayMode").Value;

        if (style == DisplayStyle.bossbar) DisplayBossbar(server);
        if (style == DisplayStyle.actionbar_text) DisplayActionBarAsText(server);
        if (style == DisplayStyle.actionbar_progressbar) DisplayActionBarAsProgress(server);
        if (style == DisplayStyle.expriencebar) DisplayExperiencebar(server);
    }

    private static ServerBossEvent bossbar = null;
    private static void DisplayBossbar(MinecraftServer server) {
        if (bossbar == null) bossbar = new ServerBossEvent(
            Component.literal((CountDownTicks / 20) + " seconds remaining"),
            BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS
        );
        bossbar.setCreateWorldFog(false);
        bossbar.setDarkenScreen(false);
        bossbar.setPlayBossMusic(false);

        bossbar.setName(Component.literal(((CountDownTicks / 20) + 1) + " seconds remaining"));
        int maxTicks = (int)Setting.GetSettingByName("TimerDuration").Value * 20;
        float progress = (float)CountDownTicks / maxTicks;
        bossbar.setProgress(progress);

        for (ServerPlayer player : server.getPlayerList().getPlayers())
            if (!bossbar.getPlayers().contains(player))
                bossbar.addPlayer(player);
    }
    private static void DisplayActionBarAsText(@NotNull MinecraftServer server) {
        Component text = Component.literal("§6" + ((CountDownTicks / 20) + 1) + " §rseconds remaining");
        server.getPlayerList().getPlayers().forEach(x -> x.displayClientMessage(text, true));
    }
    private static void DisplayActionBarAsProgress(@NotNull MinecraftServer server) {
        String text = "[";
        int squareCount = (int)(20 - (((float)CountDownTicks / ((int)Setting.GetSettingByName("TimerDuration").Value * 20)) * 20));
        String squares = "■".repeat(Math.max(squareCount, 0)); text += squares;
        text += "-".repeat(20 - (Math.max(squareCount, 0)));
        text += "]";

        String finalText = text;
        server.getPlayerList().getPlayers().forEach(x -> x.displayClientMessage(Component.literal(finalText), true));
    }
    private static void DisplayExperiencebar(@NotNull MinecraftServer server) {
        int maxTicks = (int)Setting.GetSettingByName("TimerDuration").Value * 20;
        int remainingTicks = maxTicks - CountDownTicks;
        float progress = (float) remainingTicks / maxTicks;
        server.getPlayerList().getPlayers().forEach(player -> {
            player.setExperienceLevels(maxTicks / 20 - remainingTicks / 20);
            player.setExperiencePoints((int)(progress * player.getXpNeededForNextLevel()));
        });
    }

}
