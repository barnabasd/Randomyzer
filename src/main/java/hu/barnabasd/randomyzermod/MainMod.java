package hu.barnabasd.randomyzermod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import hu.barnabasd.randomyzermod.Configuration.BookPage;
import hu.barnabasd.randomyzermod.Configuration.Converter;
import hu.barnabasd.randomyzermod.Configuration.Property;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod(MainMod.MODID)
public class MainMod {
    public static final String MODID = "randomyzermod";
    public MainMod() { MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,
            new ForgeConfigSpec.Builder().build());
    }

    @SubscribeEvent
    public void onCommandRegister(@NotNull RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("randomyzer").executes(this::mainCommand)
            .then(Commands.argument("identifier", StringArgumentType.word()).executes(this::openProperty)
                .then(Commands.argument("intcommand", StringArgumentType.word()).executes(this::modIntProperty))
                .then(Commands.argument("boolcommand", BoolArgumentType.bool()).executes(this::modBoolProperty))));
    }

    private int modBoolProperty(CommandContext<CommandSourceStack> context) {
        String propertyId = StringArgumentType.getString(context, "identifier");
        boolean command = BoolArgumentType.getBool(context, "boolcommand");
        Property<Boolean> property = (Property<Boolean>)Property.Configuration.
            values().stream().filter(x -> Objects.equals(x.Id, propertyId)).findAny().get();
        property.Value = command;
        Converter.OpenBook(BookPage.FromProperty(property), context.getSource().getPlayer());
        return 1;
    }

    private int modIntProperty(CommandContext<CommandSourceStack> context) {
        String propertyId = StringArgumentType.getString(context, "identifier");
        String command = StringArgumentType.getString(context, "intcommand");
        Property<Integer> property = (Property<Integer>)Property.Configuration.
            values().stream().filter(x -> Objects.equals(x.Id, propertyId)).findAny().get();
        if (command.startsWith("-") || command.startsWith("+"))
            property.Value += Integer.parseInt(command);
        else
            property.Value = Integer.parseInt(command);
        Converter.OpenBook(BookPage.FromProperty(property), context.getSource().getPlayer());
        return 1;
    }

    private int openProperty(CommandContext<CommandSourceStack> context) {
        String propertyId = StringArgumentType.getString(context, "identifier");
        Property<?> Property = hu.barnabasd.randomyzermod.Configuration.Property.Configuration.
            values().stream().filter(x -> Objects.equals(x.Id, propertyId)).findAny().get();
        Converter.OpenBook(BookPage.FromProperty(Property), context.getSource().getPlayer());
        return 1;
    }

    private int mainCommand(@NotNull CommandContext<CommandSourceStack> context) {
        Converter.OpenBook(BookPage.FromProperties(), context.getSource().getPlayer());
        return 1;
    }
}
