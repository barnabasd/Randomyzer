package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.CountdownDisplay;

import java.util.List;

public class ProjectStrings {

    public static final List<Setting<?, ?>> Options = List.of(
        new Setting<>(ProjectStrings.TimerDisplayId, CountdownDisplay.DisplayStyle.bossbar, CountdownDisplay.DisplayStyle.values(),
                (x, y) -> CountdownDisplay.ClearDisplays(x.getSource().getServer())),
        new Setting<>(ProjectStrings.TimerSecondsId, 20, Integer.MAX_VALUE, (x, y) -> CountdownDisplay.CountDownTicks = y * 20),
        new Setting<>(ProjectStrings.ItemCountId, 1, 2304),
        new Setting<>(ProjectStrings.GiveTypeId, RandomGen.GenType.random_individual, RandomGen.GenType.values())
    );

    public static final String GiveTypeId     = "ItemDistributionMethod",
                               TimerDisplayId = "TimerDisplayMode",
                               TimerSecondsId = "TimerDuration",
                               ItemCountId    = "ItemQuantity";

    public static final String MainCommand = "randomyzer",
                               Filtering   = "filters",
                               StartStop   = "toggle",
                               Config      = "config",
                               RunOnce     = "give";
}
