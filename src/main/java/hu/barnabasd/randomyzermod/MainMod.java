package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hu.barnabasd.randomyzermod.config.Configuration;
import hu.barnabasd.randomyzermod.config.SelectionSetting;
import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.CountdownDisplay;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod("randomyzermod")
public class MainMod {

    public static final List<Setting<?>> Options = List.of(
        new SelectionSetting<>("TimerDisplayMode", CountdownDisplay.DisplayStyle.bossbar, CountdownDisplay.DisplayStyle.values(),
                (x, y) -> CountdownDisplay.ClearDisplays(y.getSource().getServer())),
        new Setting<>("ItemQuantity", 1), new Setting<>("TimerDuration", 20, (x, y) -> CountdownDisplay.CountDownTicks = x),
        new SelectionSetting<>("ItemDistributionMethod", RandomGen.GenType.random_individual, RandomGen.GenType.values())
    );

    public MainMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        CountdownDisplay.onServerTick(event);
    }

    @SubscribeEvent
    public void onCommandRegister(@NotNull RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> mainCommand = Commands.literal("randomyzer")
            .then(Configuration.CreateCommand())
            .then(PlayerFiltering.CreateCommand())
            .then(Commands.literal("toggle").executes(c -> {
                CountdownDisplay.IsPaused = !CountdownDisplay.IsPaused;
                return 1;
            }))
            .then(Commands.literal("give").executes(c -> {
                RandomGen.RunCycle(c.getSource().getServer());
                return 1;
            }));

        event.getDispatcher().register(mainCommand);
    }

}
