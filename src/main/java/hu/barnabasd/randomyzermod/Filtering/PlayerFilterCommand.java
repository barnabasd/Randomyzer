package hu.barnabasd.randomyzermod.Filtering;

import hu.barnabasd.randomyzermod.ConfCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hu.barnabasd.randomyzermod.Messages;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import com.mojang.brigadier.StringReader;
import org.jetbrains.annotations.NotNull;
import net.minecraft.commands.Commands;

import java.util.List;
import java.util.Objects;

public class PlayerFilterCommand {

    private static void Debug(Object s, @NotNull MinecraftServer ms) {
        ms.getPlayerList().getPlayers().forEach(x -> x.displayClientMessage(Component.literal(String.valueOf(s)), false));
    }

    public static List<ServerPlayer> GetFilteredPlayers(@NotNull MinecraftServer server) {
        List<ServerPlayer> selectedPlayers = List.of();
        List<ServerPlayer> correctlyFiltered = selectedPlayers;
        List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();
        try {
            selectedPlayers = EntityArgument.players().parse(new StringReader(appliedFilter))
                .findPlayers(server.createCommandSourceStack());
        } catch (Exception ignored) { correctlyFiltered = allPlayers; }
        if (!isFilterExcluding && appliedFilter != null)
            correctlyFiltered = allPlayers.stream().filter(selectedPlayers::contains).toList();
        return correctlyFiltered;
    }

    public static boolean isFilterExcluding = true;
    public static String appliedFilter = null;

    public static void CreateCommand(@NotNull LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal("players")
            .then(Commands.literal("selector").executes(PlayerFilterCommand::GetSelector)
                .then(Commands.literal("reset").executes(PlayerFilterCommand::ResetSelector))
                .then(Commands.literal("set").then(Commands.argument("selector", EntityArgument.players())
                        .executes(PlayerFilterCommand::SetSelector))))
            .then(Commands.literal("type").executes(PlayerFilterCommand::GetType)
                .then(Commands.literal("set")
                    .then(Commands.literal("excludePlayers").executes(c -> SetType(c, true)))
                    .then(Commands.literal("includePlayers").executes(c -> SetType(c, false))))));
    }

    private static int SetType(@NotNull CommandContext<CommandSourceStack> c, boolean newValue) {
        isFilterExcluding = newValue;
        String value = isFilterExcluding ? "excludePlayers" : "includePlayers";
        Messages.SendSet(Objects.requireNonNull(c.getSource().getPlayer()),
            new ConfCommand.Property<>("playerFilterType", value));
        return 0;
    }
    private static int GetType(@NotNull CommandContext<CommandSourceStack> c) {
        String value = isFilterExcluding ? "excludePlayers" : "includePlayers";
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new ConfCommand.Property<>("playerFilterType", value));
        return 1;
    }

    private static int SetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        String selectorString = c.getInput().split(" ")[c.getInput().split(" ").length - 1];
        appliedFilter = selectorString;
        Messages.SendSet(Objects.requireNonNull(c.getSource().getPlayer()),
            new ConfCommand.Property<>("playerFilterSelector", selectorString));
        return 1;
    }
    private static int ResetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        appliedFilter = null; isFilterExcluding = true;
        Messages.SendReset(Objects.requireNonNull(c.getSource().getPlayer()),
            new ConfCommand.Property<>("playerFilterSelector", "null (don't filter)"));
        return 1;
    }
    private static int GetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        String value = (appliedFilter == null) ? "null (don't filter)" : appliedFilter;
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new ConfCommand.Property<>("playerFilterSelector", value));
        return 1;
    }

}
