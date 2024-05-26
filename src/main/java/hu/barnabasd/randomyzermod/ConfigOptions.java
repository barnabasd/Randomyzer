package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.CountdownDisplay;

import java.util.List;

public class ConfigOptions {

    public static final List<Setting<?, ?>> Options = List.of(
        new Setting<>(ConfigOptions.TimerDisplayId, CountdownDisplay.DisplayStyle.bossbar, CountdownDisplay.DisplayStyle.values(),
                (x, y) -> CountdownDisplay.ClearDisplays(x.getSource().getServer())),
        new Setting<>(ConfigOptions.TimerSecondsId, 20, Integer.MAX_VALUE, (x, y) -> CountdownDisplay.CountDownTicks = y * 20),
        new Setting<>(ConfigOptions.ItemCountId, 1, 2304),
        new Setting<>(ConfigOptions.GiveTypeId, RandomGen.GenType.random_individual, RandomGen.GenType.values())
    );

    public static final String GiveTypeId     = "ItemDistributionMethod",
                               TimerDisplayId = "TimerDisplayMode",
                               TimerSecondsId = "TimerDuration",
                               ItemCountId    = "ItemQuantity";
}
