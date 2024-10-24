package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingMode;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.List;

public class TickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::checkKeys);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::adjustInventory);

        ClientTickEvents.START_WORLD_TICK.register(TickEvents::automaticRefilling);

        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::captureInventory);
        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::loadProfile);
    }

    private static void checkKeys(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (AutomaticRefillingHandler.getSelectedSlot() != InteractionHandler.getSelectedSlot()) {
            AutomaticRefillingHandler.reset();
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed() && ConfigManager.PROFILES_STATUS.is(Status.ENABLED)) {
            client.setScreen(new ProfilesScreen());
        }
    }

    private static void adjustInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (ContextManager.isInit()) LockedSlotsHandler.adjustInventory();
    }

    private static void automaticRefilling(ClientWorld ignoredWorld) {
        if (InventiveInventory.getPlayer().isInCreativeMode()) return;
        if (AutomaticRefillingMode.isValid() && ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && ContextManager.isInit() && AutomaticRefillingHandler.shouldRun()) {
            ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
            AutomaticRefillingHandler.runMainHand();
            ContextManager.setContext(Contexts.INIT);
        }
        if (AutomaticRefillingMode.isValid() && ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.ENABLED) && ContextManager.isInit() && AutomaticRefillingHandler.shouldRunOffHand()) {
            ContextManager.setContext(Contexts.AUTOMATIC_REFILLING);
            AutomaticRefillingHandler.runOffHand();
            ContextManager.setContext(Contexts.INIT);
        }
    }

    private static void captureInventory(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        LockedSlotsHandler.setSavedInventory();
        LockedSlotsHandler.setSavedHandlerInventory();
        AutomaticRefillingHandler.setMainHandStack(client.player.getMainHandStack());
        AutomaticRefillingHandler.setOffHandStack(client.player.getOffHandStack());
        AutomaticRefillingHandler.setSelectedSlot(InteractionHandler.getSelectedSlot());
    }

    private static void loadProfile(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        for (KeyBinding profileKey : KeyRegistry.profileKeys) {
            if (profileKey.isPressed()) {
                boolean validMode = ConfigManager.FAST_LOAD.is(true) || (ConfigManager.FAST_LOAD.is(false) && KeyRegistry.loadProfileKey.isPressed());
                if (validMode && ContextManager.isInit()) {
                    ContextManager.setContext(Contexts.PROFILES);
                    List<Profile> profiles = ProfileHandler.getProfiles();
                    profiles.forEach(profile -> {
                        if (profileKey.getTranslationKey().equals(profile.getKey())) ProfileHandler.load(profile);
                    });
                    ContextManager.setContext(Contexts.INIT);
                }
            }
        }
    }
}
