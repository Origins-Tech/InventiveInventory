package net.origins.inventive_inventory.commands.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.commands.config.type.ConfigArgumentType;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import net.origins.inventive_inventory.config.options.ConfigOption;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesConfigScreen;
import net.origins.inventive_inventory.util.Notifier;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(accessor("Sorting", ConfigType.SORTING))
                .then(accessor("AutomaticRefilling", ConfigType.AUTOMATIC_REFILLING))
                .then(accessor("Profiles", ConfigType.PROFILES))
                .then(ClientCommandManager.literal("ProfilesConfigScreen")
                        .executes(context -> {
                            MinecraftClient client = context.getSource().getClient();
                            client.send(() -> client.setScreen(new ProfilesConfigScreen(null)));
                            return 1;
                        }))
        );
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> accessor(String literal, ConfigType type) {
        return ClientCommandManager.literal(literal)
                .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.argument("config", ConfigArgumentType.of(type))
                                .then(ClientCommandManager.argument("value", StringArgumentType.word())
                                        .suggests(ConfigCommand::suggest)
                                        .executes(ConfigCommand::setConfig)
                                )
                        )
                )
                .then(ClientCommandManager.literal("get")
                        .then(ClientCommandManager.argument("config", ConfigArgumentType.of(type))
                                .executes(ConfigCommand::getInfo)
                        )
                );
    }

    @SuppressWarnings("unchecked")
    private static <T> CompletableFuture<Suggestions> suggest(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        ConfigOption<T> configOption = (ConfigOption<T>) ConfigArgumentType.getConfig(context, "config");
        if (configOption != null) Arrays.asList(configOption.getValues()).forEach(option -> builder.suggest(ConfigOption.getValueAsText(option).getString()));
        return builder.buildFuture();
    }

    @SuppressWarnings("unchecked")
    private static <T> int setConfig(CommandContext<FabricClientCommandSource> context) {
        String value = StringArgumentType.getString(context, "value");
        ConfigOption<T> option = (ConfigOption<T>) ConfigArgumentType.getConfig(context, "config");
        if (option == null) {
            Notifier.error("Invalid config option!");
            return -1;
        } else if (ConfigOption.getValueAsText(option.getValue()).getString().equals(value)) {
            Notifier.error("Already set to: " + value);
            return -1;
        }
        for (T optionValue : option.getValues()) {
            if (ConfigOption.getValueAsText(optionValue).getString().equals(value)) {
                option.setValue(value);
                Notifier.send("Set to: " + value, Formatting.GREEN);
                return 1;
            }
        }
        Notifier.error("Invalid config value!");
        return -1;
    }

    @SuppressWarnings("unchecked")
    private static <T> int getInfo(CommandContext<FabricClientCommandSource> context) {
        ConfigOption<T> option = (ConfigOption<T>) ConfigArgumentType.getConfig(context, "config");
        if (option == null) {
            Notifier.error("Invalid config option!");
            return -1;
        }
        Notifier.send(Text.translatable(option.getTranslationKey()).getString() + ": " + ConfigOption.getValueAsText(option.getValue()).getString(), Formatting.BLUE);
        return 1;
    }
}
