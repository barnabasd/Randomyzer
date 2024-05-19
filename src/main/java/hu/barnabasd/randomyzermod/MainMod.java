package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hu.barnabasd.randomyzermod.Configuration.ConfCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import com.mojang.brigadier.CommandDispatcher;
import org.jetbrains.annotations.NotNull;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.commands.Commands;

import java.util.List;

@Mod(MainMod.MODID)
public class MainMod {

    public static final List<ConfCommand.Property<?>> Options = List.of(
        new ConfCommand.EnumProperty<>("CountdownStyle", CountdownDisplay.DisplayStyle.bossbar, CountdownDisplay.DisplayStyle.values()),
        new ConfCommand.EnumProperty<>("GiveBehaviour", RandomGen.GenType.random_individual, RandomGen.GenType.values()),
        new ConfCommand.Property<>("GiveAmount", 1), new ConfCommand.Property<>("CountdownTime", 20)
    );

    public static final String MODID = "randomyzermod";
    public MainMod() { MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,
            new ForgeConfigSpec.Builder().build());
    }

    @SubscribeEvent
    public void onCommandRegister(@NotNull RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> config = Commands.literal("config");
        ConfCommand.AppendPropertyNamesToCommand(config);

        LiteralArgumentBuilder<CommandSourceStack> mainCommand =
            Commands.literal("randomyzer").then(config);

        dispatcher.register(mainCommand);
    }
}
