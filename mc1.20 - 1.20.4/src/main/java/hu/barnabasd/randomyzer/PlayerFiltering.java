package hu.barnabasd.randomyzer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PlayerFiltering {

    private static String filter = "@a";

    public static @NotNull List<ServerPlayer> GetAllApplicablePlayers(CommandSourceStack c) {
        try { return EntityArgument.players().parse(new StringReader(filter)).findPlayers(c); }
        catch (CommandSyntaxException x) { x.printStackTrace(); }
        System.out.println("Very not good");
        return c.getServer().getPlayerList().getPlayers();
    }

    public static LiteralArgumentBuilder<CommandSourceStack> GetCommand() {
        return Commands.literal("players").executes(PlayerFiltering::GetFilter)
            .then(Commands.argument("value", EntityArgument.players())
                .executes(PlayerFiltering::SetFilter));
    }

    private static int SetFilter(@NotNull CommandContext<CommandSourceStack> context) {
        filter = context.getInput().split(" ")[context.getInput().split(" ").length - 1];
        Objects.requireNonNull(context.getSource().getPlayer()).sendSystemMessage(
            Component.literal("Setting players is now set to: " + filter));
        Countdown.HideAll(context.getSource().getServer());
        return 1;
    }

    private static int GetFilter(@NotNull CommandContext<CommandSourceStack> context) {
        Objects.requireNonNull(context.getSource().getPlayer()).sendSystemMessage(
            Component.literal("Setting players is currently set to: " + filter));
        return 1;
    }

}
