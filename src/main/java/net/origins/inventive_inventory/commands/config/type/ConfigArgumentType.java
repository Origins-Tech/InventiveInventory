package net.origins.inventive_inventory.commands.config.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.options.ConfigOption;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

public class ConfigArgumentType implements ArgumentType<ConfigOption<?>> {
    private final ConfigType configType;

    private ConfigArgumentType(ConfigType configType) {
        this.configType = configType;
    }

    public static ConfigArgumentType of(ConfigType configType) {
        return new ConfigArgumentType(configType);
    }

    public static ConfigOption<?> getConfig(CommandContext<?> context, String name) {
        try {
            return context.getArgument(name, ConfigOption.class);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public ConfigOption<?> parse(StringReader reader) throws CommandSyntaxException {
        String str = reader.readString();
        for (Field field : ConfigManager.class.getDeclaredFields()) {
            if (ConfigOption.class.isAssignableFrom(field.getType())) {
                try {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                    if (Text.translatable(option.getTranslationKey()).getString().replaceAll(" ", "").equals(str)) {
                        return option;
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Field field : ConfigManager.class.getDeclaredFields()) {
            if (ConfigOption.class.isAssignableFrom(field.getType())) {
                try {
                    ConfigOption<?> option = ((ConfigOption<?>) field.get(null));
                    if (this.configType == option.getConfigType()) {
                        builder.suggest(Text.translatable(option.getTranslationKey()).getString().replaceAll(" ", ""));
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return builder.buildFuture();
    }
}
