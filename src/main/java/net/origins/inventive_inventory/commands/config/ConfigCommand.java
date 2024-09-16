package net.origins.inventive_inventory.commands.config;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.screens.ConfigScreen;

public class ConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal(InventiveInventory.MOD_ID)
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            MinecraftClient client = context.getSource().getClient();
                            client.send(() -> client.setScreen(new ConfigScreen(null)));
                            return 1;
                        }))
        );
    }
}
