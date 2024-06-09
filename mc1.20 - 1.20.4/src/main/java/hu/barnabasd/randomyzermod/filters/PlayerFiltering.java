package hu.barnabasd.randomyzermod.filters;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.display.Messages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerFiltering {

    public static List<ServerPlayer> availablePlayers = new ArrayList<>();

    public static @NotNull LiteralArgumentBuilder<CommandSourceStack> CreateCommand() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("players");
        command.then(
            Commands.literal("add").then(Commands.argument("selector", EntityArgument.players()).executes(PlayerFiltering::AddPlayer))).then(
            Commands.literal("remove").then(Commands.argument("selector", EntityArgument.players()).executes(PlayerFiltering::RmPlayer))).then(
            Commands.literal("reset").executes(PlayerFiltering::Reset)).then(Commands.literal("list").executes(PlayerFiltering::ListPlayers)
        );
        return command;
    }

    private static int ListPlayers(CommandContext<CommandSourceStack> context) {
        if (availablePlayers.isEmpty())
            Messages.SendEmptyPlayerFilters(Objects.requireNonNull(context.getSource().getPlayer()));
        else
            Messages.SendPlayerFilters(Objects.requireNonNull(context.getSource().getPlayer()), availablePlayers);
        return 1;
    }

    private static int Reset(@NotNull CommandContext<CommandSourceStack> context) {
        availablePlayers = context.getSource().getServer().getPlayerList().getPlayers();
        Objects.requireNonNull(context.getSource().getPlayer())
            .sendSystemMessage(Component.literal("Successfully reset players."));
        return 1;
    }

    private static int RmPlayer(CommandContext<CommandSourceStack> context) {
        try {
            List<ServerPlayer> operatedPlayer_s = EntityArgument.getPlayers(context, "selector").stream().toList();
            availablePlayers.removeAll(operatedPlayer_s);
        } catch (Exception ignored) { }
        return 1;
    }

    private static int AddPlayer(CommandContext<CommandSourceStack> context) {
        try {
            List<ServerPlayer> operatedPlayer_s = EntityArgument.getPlayers(context, "selector").stream().toList();
            availablePlayers.addAll(operatedPlayer_s);
        } catch (Exception ignored) {}
        return 1;
    }

}
