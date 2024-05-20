package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hu.barnabasd.randomyzermod.Filtering.PlayerFilterCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.commands.Commands;

import java.util.List;

@Mod("randomyzermod")
public class MainMod {

    public static final List<ConfCommand.Property<?>> Options = List.of(
        new ConfCommand.EnumProperty<>("CountdownStyle", CountdownDisplay.DisplayStyle.bossbar,
            CountdownDisplay.DisplayStyle.values(), (x) -> CountdownDisplay.ClearOnNextTick = true),
        new ConfCommand.EnumProperty<>("GiveBehaviour", RandomGen.GenType.random_individual, RandomGen.GenType.values()),
        new ConfCommand.Property<>("GiveAmount", 1), new ConfCommand.Property<>("CountdownTime", 20,
            (x) -> CountdownDisplay.CountDownTicks = (int)x)
    );

    public MainMod() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,
            new ForgeConfigSpec.Builder().build());
    }

    @SubscribeEvent
    public void onCommandRegister(@NotNull RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> config = Commands.literal("config");
        ConfCommand.CreateCommand(config);

        LiteralArgumentBuilder<CommandSourceStack> filters = Commands.literal("filters");
        PlayerFilterCommand.CreateCommand(filters);

        LiteralArgumentBuilder<CommandSourceStack> mainCommand =
            Commands.literal("randomyzer")
                .then(config).then(filters)
                .then(Commands.literal("toggle").executes(c -> { CountdownDisplay.IsPaused = !CountdownDisplay.IsPaused; return 1; }))
                .then(Commands.literal("cycle").executes(c -> { RandomGen.RunCycle(c.getSource().getServer()); return 1; }));

        event.getDispatcher().register(mainCommand);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        CountdownDisplay.onServerTick(event);
    }
}
