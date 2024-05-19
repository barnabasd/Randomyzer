# Basic Commands
|Command|Explanation|
|-------|-----------|
|`/randomyzer toggle`|Toggles if the countdown is active.|
|`/randomyzer cycle`|Runs the cycle once with the set properties|
# Configuration
|Command|Action|
|-------|------|
|`/randomyzer config <property>`|Gets the value of `<property>`.|
|`/randomyzer config <property> reset`|Resets the value of `<property>`.|
|`/randomyzer config <property> set <...>`|Sets the value of `<property>`. Possible values listed below.|
# Properties
|Property|Default Value|Explanation|
|--------|-------------|-----------|
|`CountdownTime`|20 seconds|Controls how much time should be between the cycles. (In seconds)|
|`GiveAmount`|One item|Controls how many items each player gets.|
|`CountdownStyle`|bossbar|Possible values: <ul><li><code>bossbar</code> Uses a bossbar on the top of the screen</li><li><code>actionbar_text</code> Uses the actionbar like: "20 seconds remaining"</li><li><code>actionbar_progressbar</code> Uses the actionbar like: "[■■■■----------------]"</li><li><code>experiencebar</code> Uses the experience bar and levels. (Careful because players can use the given experience!)</li><li><code>hidden</code> Hides all the countdowns.</li></ul>||``|.|
|`GiveBehaviour`|random_individual|Examples down below|
# GiveBehaviour Example Explanation (one cycle with 2 items)
|Name|Player1|Player2|
|----|-------|-------|
|||
|||
|||
|||
