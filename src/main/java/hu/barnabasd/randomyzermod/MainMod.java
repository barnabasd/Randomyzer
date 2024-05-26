package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hu.barnabasd.randomyzermod.config.Configuration;
import hu.barnabasd.randomyzermod.display.CountdownDisplay;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod("randomyzermod")
public class MainMod {

    public MainMod() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(CountdownDisplay::onServerTick);
    }

    @SubscribeEvent
    public void onCommandRegister(@NotNull RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> mainCommand = Commands.literal("randomyzer")
            .requires(x -> x.hasPermission(4))
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
