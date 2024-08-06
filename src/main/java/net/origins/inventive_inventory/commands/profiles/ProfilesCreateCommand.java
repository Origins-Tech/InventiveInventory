package net.origins.inventive_inventory.commands.profiles;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.command.CommandRegistryAccess;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.util.Notifier;

import java.util.concurrent.CompletableFuture;

public class ProfilesCreateCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-profiles")
                .then(ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                .executes(ProfilesCreateCommand::create)
                                .then(ClientCommandManager.argument("keybinding", StringArgumentType.word())
                                        .suggests(ProfilesCreateCommand::getKeyBinds)
                                        .executes(ProfilesCreateCommand::createWithKeyBinding)
                                )
                        )
                )
        );
    }

    private static int create(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        if (!name.isEmpty() && ProfileHandler.isNoProfile(name)) {
            ProfileHandler.create(name, "");
            return 1;
        }
        Notifier.error("Profile needs an exclusive name!");
        return -1;
    }

    private static int createWithKeyBinding(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String keyBinding = StringArgumentType.getString(context, "keybinding");
        if (!name.isEmpty() && ProfileHandler.isNoProfile(name)) {
            for (KeyBinding key : ProfileHandler.getAvailableProfileKeys()) {
                if (key.getBoundKeyLocalizedText().getString().equals(keyBinding)) {
                    ProfileHandler.create(name, key.getTranslationKey());
                    return 1;
                }
            }
            Notifier.error("Key is not available!");
            return -1;
        }
        Notifier.error("Profile needs a name!");
        return -1;
    }

    private static CompletableFuture<Suggestions> getKeyBinds(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        ProfileHandler.getAvailableProfileKeys().forEach(keyBinding -> builder.suggest(keyBinding.getBoundKeyLocalizedText().getString()));
        return builder.buildFuture();
    }
}
