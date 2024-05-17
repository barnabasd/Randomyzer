package hu.barnabasd.randomyzermod.Configuration;

import ...;

public class ConfCommand {

    public static void UpdatePropertyValue(CommandContext<CommandSourceStack> context, Property<?> property) {

    }

    public static void AppendPropertyNamesToCommandHandlerByDefinedFinalPropertiesDictionary(LiteralArgumentBuilder<CommandSourceStack> command) {
        List<Property> Properties = Property.Configuration.values();
        for (Property property : Properties) {
            command.then(Commands.literal(property.Id).executes(context -> UpdatePropertyValue(context, property)));
        }
    }

}