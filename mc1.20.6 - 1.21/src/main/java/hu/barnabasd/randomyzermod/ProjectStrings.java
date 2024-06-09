package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.CountdownDisplay;

import java.util.List;

public class ProjectStrings {

    public static final List<Setting<?, ?>> Options = List.of(
        new Setting<>(ProjectStrings.TimerDisplayId, DisplayStyle.bossbar, DisplayStyle.values(),
            (x, y) -> CountdownDisplay.ClearDisplays(x.getSource().getServer())),
        new Setting<>(ProjectStrings.TimerSecondsId, 20, Integer.MAX_VALUE, (x, y) -> CountdownDisplay.CountDownTicks = y * 20),
        new Setting<>(ProjectStrings.GiveTypeId, DistributionType.randomMultipleItems, DistributionType.values()),
        new Setting<>(ProjectStrings.CycleSoundId, CycleSound.experience, CycleSound.values()),
        new Setting<>(ProjectStrings.ItemCountId, 1, 2304)
    );

    public static final String
        GiveTypeId     = "itemDistribution",
        TimerDisplayId = "timerDisplayMode",
        TimerSecondsId = "timerDuration",
        ItemCountId    = "itemQuantity",
        CycleSoundId   = "cycleSound";

    public static final String
        MainCommand = "randomyzer",
        filtering = "filters",
        StartStop   = "toggle",
        Config      = "config",
        RunOnce     = "give";

    public enum DistributionType { randomMultipleItems, randomSameItem, sameMultipleItems, sameSameItem }
    public enum DisplayStyle { bossbar, actionbarAsText, actionbarAsProgress, experience, hidden }
    public enum CycleSound { experience, itemPickup, none }
}
