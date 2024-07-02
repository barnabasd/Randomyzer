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
        event.getDispatcher().register(RandomyzerCommand.MAIN.requires(x -> x.hasPermission(4)));
    }

    private void onServerTick(@NotNull TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !IsTimerRunning) return;
        if (CountDownTicks > 1) CountDownTicks--;
        else {
            ItemAlgorithms.Execute(PlayerFiltering.GetAllApplicablePlayers(
                event.getServer().createCommandSourceStack()));
            CountDownTicks = RandomyzerCommand.countdownTime.GetValue() * 20;
            DropSound.Execute(PlayerFiltering.GetAllApplicablePlayers(
                event.getServer().createCommandSourceStack()));
        }
        if (RandomyzerCommand.countdownStyle.GetValue()
            != RandomyzerCommand.TimerDisplayType.none)
                Countdown.Execute(PlayerFiltering.GetAllApplicablePlayers(
                    event.getServer().createCommandSourceStack()));
        else Countdown.HideAll(event.getServer());
    }

}
