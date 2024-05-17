package hu.barnabasd.randomyzermod.Configuration;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.context.CommandContext;

import java.util.List;

public class ConfCommand {

    public static int UpdatePropertyValue(CommandContext<CommandSourceStack> context, Property<?> property) {
        return 1;
    }

    public static void AppendPropertyNamesToCommandHandlerByDefinedFinalPropertiesDictionary(LiteralArgumentBuilder<CommandSourceStack> command) {
        List<Property<?>> Properties = Property.Configuration.values().stream().toList();
        for (Property<?> property : Properties) {
            command.then(Commands.literal(property.Id).executes(context -> UpdatePropertyValue(context, property)));
        }
    }

}