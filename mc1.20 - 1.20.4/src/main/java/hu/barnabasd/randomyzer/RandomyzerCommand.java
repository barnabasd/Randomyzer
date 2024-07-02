package hu.barnabasd.randomyzer;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RandomyzerCommand {

    public enum DistributionType { randomDifferent, sameDifferent, randomGlobal, sameGlobal }
    public enum TimerDisplayType { bossbar, actionbar, none }

    public static final CustomGameRule.IntegerRule countdownTime =
        new CustomGameRule.IntegerRule("countdownTime", 1, (Integer.MAX_VALUE / 20) - 1, 40,
            (e, f) -> Randomyzer.CountDownTicks = Math.min(Randomyzer.CountDownTicks, f * 20));
    public static final CustomGameRule.EnumRule<TimerDisplayType> countdownStyle =
        new CustomGameRule.EnumRule<>("countdownStyle", TimerDisplayType.values(), TimerDisplayType.bossbar, (e, f) -> Countdown.HideAll(e));
    public static final CustomGameRule.EnumRule<DistributionType> distributionType =
        new CustomGameRule.EnumRule<>("distributionType", DistributionType.values(), DistributionType.randomDifferent);
    public static final CustomGameRule.BooleanRule enableDropSound =
        new CustomGameRule.BooleanRule("enableDropSound", true);
    public static final CustomGameRule.IntegerRule itemCount =
        new CustomGameRule.IntegerRule("itemCount", 0, 64, 1);

    public static final List<CustomGameRule<?>> PROPERTIES =
        List.of(distributionType, countdownStyle, countdownTime, enableDropSound, itemCount);

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> GetConfig() {
        LiteralArgumentBuilder<CommandSourceStack> Config = Commands.literal("config");
        PROPERTIES.forEach(property -> Config.then(property.GetCommand()));
        return Config;
    }

    public static final LiteralArgumentBuilder<CommandSourceStack> MAIN =
        Commands.literal("randomyzer")
            .then(Commands.literal("toggle").executes(c -> {
                Randomyzer.IsTimerRunning = !Randomyzer.IsTimerRunning; return 1;
            }))
            .then(Commands.literal("cycle").executes(c -> {
                    ItemAlgorithms.Execute(c.getSource().getServer()
                        .getPlayerList().getPlayers()); return 1;
                }))
            .then(GetConfig());
}
