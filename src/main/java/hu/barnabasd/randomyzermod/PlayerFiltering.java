package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("SameReturnValue")
public class PlayerFiltering {

    public static boolean isFilterExcluding = true;
    public static String appliedFilter = null;

    public static List<ServerPlayer> GetFilteredPlayers(@NotNull MinecraftServer server) {
        List<ServerPlayer> selectedPlayers = List.of();
        List<ServerPlayer> correctlyFiltered = selectedPlayers;
        List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();
        try {
            selectedPlayers = EntityArgument.players().parse(new StringReader(appliedFilter))
                .findPlayers(server.createCommandSourceStack());
        } catch (Exception ignored) {
            correctlyFiltered = allPlayers;
        }
        if (!isFilterExcluding && appliedFilter != null)
            correctlyFiltered = allPlayers.stream().filter(selectedPlayers::contains).toList();
        return correctlyFiltered;
    }

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("filters");
        command.then(Commands.literal("players")
            .then(Commands.literal("selector").executes(PlayerFiltering::GetSelector)
                .then(Commands.literal("reset").executes(PlayerFiltering::ResetSelector))
                .then(Commands.literal("set").then(Commands.argument("selector", EntityArgument.players())
                    .executes(PlayerFiltering::SetSelector))))
            .then(Commands.literal("type").executes(PlayerFiltering::GetType)
                .then(Commands.literal("reset").executes(PlayerFiltering::ResetType))
                .then(Commands.literal("set")
                    .then(Commands.literal("excludePlayers").executes(c -> SetType(c, true)))
                    .then(Commands.literal("includePlayers").executes(c -> SetType(c, false))))));
        return command;
    }

    private static int SetType(@NotNull CommandContext<CommandSourceStack> c, boolean newValue) {
        isFilterExcluding = newValue;
        String value = isFilterExcluding ? "excludePlayers" : "includePlayers";
        Messages.SendSet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterType", value));
        return 1;
    }

    private static int ResetType(@NotNull CommandContext<CommandSourceStack> c) {
        isFilterExcluding = true;
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterType", true));
        return 1;
    }

    private static int GetType(@NotNull CommandContext<CommandSourceStack> c) {
        String value = isFilterExcluding ? "excludePlayers" : "includePlayers";
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterType", value));
        return 1;
    }

    private static int SetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        String selectorString = c.getInput().split(" ")[c.getInput().split(" ").length - 1];
        appliedFilter = selectorString;
        Messages.SendSet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterSelector", selectorString));
        return 1;
    }

    private static int ResetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        appliedFilter = null;
        Messages.SendReset(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterSelector", "null (don't filter)"));
        return 1;
    }

    private static int GetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        String value = (appliedFilter == null) ? "null (don't filter)" : appliedFilter;
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterSelector", value));
        return 1;
    }

}
