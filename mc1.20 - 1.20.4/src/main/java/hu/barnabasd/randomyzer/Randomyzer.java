package hu.barnabasd.randomyzer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod(Randomyzer.MOD_ID)
public class Randomyzer {

    public static int CountDownTicks = RandomyzerCommand.countdownTime.GetValue() * 20;
    public static boolean IsTimerRunning = false;

    public static final String MOD_ID = "randomyzer";
    public Randomyzer() {
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
    }

    private void onCommandRegister(@NotNull RegisterCommandsEvent event) {
        event.getDispatcher().register(RandomyzerCommand.MAIN);
    }

    private void onServerTick(@NotNull TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !IsTimerRunning) return;
        if (CountDownTicks > 1) CountDownTicks--;
        else {
            ItemAlgorithms.Execute(event.getServer().getPlayerList().getPlayers());
            CountDownTicks = RandomyzerCommand.countdownTime.GetValue() * 20;
            DropSound.Execute(event.getServer().getPlayerList().getPlayers());
        }
        if (RandomyzerCommand.countdownStyle.GetValue()
            != RandomyzerCommand.TimerDisplayType.none)
                Countdown.Execute(event.getServer().getPlayerList().getPlayers());
        else Countdown.HideAll(event.getServer());
    }

}
