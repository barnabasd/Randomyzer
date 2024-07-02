package hu.barnabasd.randomyzer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerFiltering {

    private static String filter = "@e";

    public static @NotNull List<ServerPlayer> GetAllApplicablePlayers(CommandSourceStack c) {
        try { return EntityArgument.players().parse(new StringReader(filter)).findPlayers(c); }
        catch (CommandSyntaxException x) { x.printStackTrace(); }
        return c.getServer().getPlayerList().getPlayers();
    }

    public static LiteralArgumentBuilder<CommandSourceStack> GetCommand() {
        return Commands.literal("players").executes(PlayerFiltering::GetFilter)
            .then(Commands.argument("value", EntityArgument.players())
                .executes(PlayerFiltering::SetFilter));
    }

    private static int SetFilter(CommandContext<CommandSourceStack> context) {
        return 1;
    }

    private static int GetFilter(CommandContext<CommandSourceStack> context) {
        return 1;
    }

}
