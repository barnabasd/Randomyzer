package hu.barnabasd.randomyzermod.Configuration;

import ...;

public class ConfCommand {

    public static void UpdatePropertyValue(CommandContext<CommandSourceStack> context, String id) {

    }

    public static void AppendPropertyNamesToCommandHandlerByDefinedFinalPropertiesDictionary(LiteralArgumentBuilder<CommandSourceStack> command) {
        List<String> PropertyIdentifiers = Property.Configuration.values().map(x -> x.Name);
        for (String propertyId : PropertyIdentifiers) {
            LiteralArgumentBuilder<CommandSourceStack> propertyInternalCommand = 
                Commands.literal(propertyId).executes(context -> UpdatePropertyValue(context, propertyId));

            
            command.then(propertyInternalCommand);
        }
    }

}