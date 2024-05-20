![](https://raw.githubusercontent.com/barnabasd/Randomyzer/master/images/banner_big.jpg)
# Basic Commands
| Command              | Explanation                                 |
|----------------------|---------------------------------------------|
| `/randomyzer toggle` | Toggles if the countdown is active.         |
| `/randomyzer cycle`  | Runs the cycle once with the set properties |
# Configuration
| Command                                   | Action                                                        |
|-------------------------------------------|---------------------------------------------------------------|
| `/randomyzer config <property>`           | Gets the value of `<property>`.                               |
| `/randomyzer config <property> reset`     | Resets the value of `<property>`.                             |
| `/randomyzer config <property> set <...>` | Sets the value of `<property>`. Possible values listed below. |
# Properties
| Property         | Default Value     | Explanation                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|------------------|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CountdownTime`  | 20 seconds        | Controls how much time should be between the cycles. (In seconds)                                                                                                                                                                                                                                                                                                                                                                                                     |
| `GiveAmount`     | One item          | Controls how many items each player gets.                                                                                                                                                                                                                                                                                                                                                                                                                             |
| `CountdownStyle` | bossbar           | Possible values: <ul><li><code>bossbar</code> Uses a bossbar on the top of the screen</li><li><code>actionbar_text</code> Uses the actionbar like: "20 seconds remaining"</li><li><code>actionbar_progressbar</code> Uses the actionbar like: "[■■■■----------------]"</li><li><code>experiencebar</code> Uses the experience bar and levels. (Careful because players can use the given experience!)</li><li><code>hidden</code> Hides all the countdowns.</li></ul> ||``|.|
| `GiveBehaviour`  | random_individual | Examples down below                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
# GiveBehaviour Explanation
| Name                 | Explanation                                                                                   |
|----------------------|-----------------------------------------------------------------------------------------------|
| `random_individual`  | Every player gets {GiveAmount} random different items.<br>Different for every player.         |
| `uniform_individual` | Every player gets {GiveAmount} of one item.<br>Different for every player.                    |
| `random_shared`      | Every player gets {GiveAmount} random different items.<br>Same random items for every player. |
| `uniform_shared`     | Every player gets {GiveAmount} of one item.<br>Same random items for every player.            |
