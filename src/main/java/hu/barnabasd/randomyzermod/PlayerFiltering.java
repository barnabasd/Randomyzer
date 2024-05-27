package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.config.Setting;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
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
        List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();
        List<ServerPlayer> argumentPlayers = List.of();
        String filter = appliedFilter;
        if (appliedFilter == null) filter = "@a";
        try {
            argumentPlayers = EntityArgument.players().parse(new StringReader(filter)).findPlayers(server.createCommandSourceStack());
        }
        catch (Exception ex) {
            allPlayers.forEach(x -> x.displayClientMessage(Component.literal("§4An internal error occurred!\n" + ex), false));
        }
        List<ServerPlayer> correctedList = argumentPlayers;
        if (isFilterExcluding) {
            try {
                List<ServerPlayer> playersToExclude = argumentPlayers;
                correctedList = allPlayers.stream().filter(x -> !playersToExclude.contains(x)).toList();
            }
            catch (Exception ex) {
                allPlayers.forEach(x -> x.displayClientMessage(Component.literal("§4An internal error occurred!\n" + ex), false));
            }
        }
        if (appliedFilter == null) correctedList = allPlayers;
        return correctedList;
    }

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("players");
        command.then(Commands.literal("selector").executes(PlayerFiltering::GetSelector)
                .then(Commands.literal("reset").executes(PlayerFiltering::ResetSelector))
                .then(Commands.literal("set").then(Commands.argument("selector", EntityArgument.players())
                    .executes(PlayerFiltering::SetSelector))))
            .then(Commands.literal("type").executes(PlayerFiltering::GetType)
                .then(Commands.literal("reset").executes(PlayerFiltering::ResetType))
                .then(Commands.literal("set")
                    .then(Commands.literal("exclude").executes(c -> SetType(c, true)))
                    .then(Commands.literal("include").executes(c -> SetType(c, false)))));
        return command;
    }

    private static int SetType(@NotNull CommandContext<CommandSourceStack> c, boolean newValue) {
        isFilterExcluding = newValue;
        String value = isFilterExcluding ? "exclude" : "include";
        Messages.SendSet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterType", value, 0));
        return 1;
    }

    private static int ResetType(@NotNull CommandContext<CommandSourceStack> c) {
        isFilterExcluding = true;
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterType", true, 0));
        return 1;
    }

    private static int GetType(@NotNull CommandContext<CommandSourceStack> c) {
        String value = isFilterExcluding ? "excludePlayers" : "includePlayers";
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterType", value, 0));
        return 1;
    }

    private static int SetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        String selectorString = c.getInput().split(" ")[c.getInput().split(" ").length - 1];
        appliedFilter = selectorString;
        Messages.SendSet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterSelector", selectorString, 0));
        return 1;
    }

    private static int ResetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        appliedFilter = null;
        Messages.SendReset(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterSelector", "null (don't filter)", 0));
        return 1;
    }

    private static int GetSelector(@NotNull CommandContext<CommandSourceStack> c) {
        String value = (appliedFilter == null) ? "null (don't filter)" : appliedFilter;
        Messages.SendGet(Objects.requireNonNull(c.getSource().getPlayer()),
            new Setting<>("playerFilterSelector", value, 0));
        return 1;
    }

}
