package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;

public class NewTickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(NewTickEvents::checkKeys);
        ClientTickEvents.START_WORLD_TICK.register(world -> captureMainHand(InventiveInventory.getClient()));
        ClientTickEvents.START_WORLD_TICK.register(world -> captureOffHand(InventiveInventory.getClient()));
    }


    private static void checkKeys(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (AutomaticRefillingHandler.SELECTED_SLOT != InteractionHandler.getSelectedSlot()) {
            AutomaticRefillingHandler.reset();
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed() && ConfigManager.PROFILES_STATUS.is(Status.ENABLED)) {
            client.setScreen(new ProfilesScreen());
        }
    }

    private static void captureMainHand(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            if (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed()) {
                AutomaticRefillingHandler.setMainHandStack(InteractionHandler.getMainHandStack());
            }
        }
    }

    private static void captureOffHand(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            if (client.options.useKey.isPressed()) {
                AutomaticRefillingHandler.setOffHandStack(InteractionHandler.getOffHandStack());
            }
        }
    }

    private static void adjustInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (ContextManager.isInit()) LockedSlotsHandler.adjustInventory();
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null && (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed())) {
            if (ConfigManager.AUTOMATIC_REFILLING_MODE.getValue().isValid() && ContextManager.isInit()) {
                ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
                AutomaticRefillingHandler.runMainHand();
                if (AutomaticRefillingHandler.RUN_OFFHAND) {
                    AutomaticRefillingHandler.runOffHand();
                }
                ContextManager.setContext(Contexts.INIT);
            }
        }
    }
}
