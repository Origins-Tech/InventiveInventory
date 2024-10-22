package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingMode;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandlerNew;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;

public class NewTickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(NewTickEvents::checkKeys);
        ClientTickEvents.START_CLIENT_TICK.register(NewTickEvents::adjustInventory);

        ClientTickEvents.START_WORLD_TICK.register(NewTickEvents::automaticRefilling);

        ClientTickEvents.END_CLIENT_TICK.register(NewTickEvents::captureInventory);
    }

    private static void checkKeys(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (AutomaticRefillingHandlerNew.getSelectedSlot() != InteractionHandler.getSelectedSlot()) {
            AutomaticRefillingHandlerNew.reset();
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed() && ConfigManager.PROFILES_STATUS.is(Status.ENABLED)) {
            client.setScreen(new ProfilesScreen());
        }
    }

    private static void adjustInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (ContextManager.isInit()) LockedSlotsHandler.adjustInventory();
    }

    private static void automaticRefilling(ClientWorld world) {
        if (InventiveInventory.getPlayer().isInCreativeMode()) return;
        if (AutomaticRefillingMode.isValid() && ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && ContextManager.isInit() && AutomaticRefillingHandlerNew.shouldRun()) {
            ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
            AutomaticRefillingHandlerNew.runMainHand();
            ContextManager.setContext(Contexts.INIT);
        }
        if (AutomaticRefillingMode.isValid() && ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && ContextManager.isInit() && AutomaticRefillingHandlerNew.shouldRunOffHand()) {
            ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
            AutomaticRefillingHandlerNew.runOffHand();
            ContextManager.setContext(Contexts.INIT);
        }
    }

    private static void captureInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        LockedSlotsHandler.setSavedInventory();
        LockedSlotsHandler.setSavedHandlerInventory();
        AutomaticRefillingHandlerNew.setMainHandStack(client.player.getMainHandStack());
        AutomaticRefillingHandlerNew.setOffHandStack(client.player.getOffHandStack());
        AutomaticRefillingHandlerNew.setSelectedSlot(InteractionHandler.getSelectedSlot());
    }
}
