package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.config.enums.automatic_refilling.ToolReplacementBehaviour;
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
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureMainHand);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureOffHand);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::adjustInventory);

        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::automaticRefilling);
        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::loadProfile);
        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::captureInventory);
    }

    private static void checkKeys(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (AutomaticRefillingHandler.SELECTED_SLOT != InteractionHandler.getSelectedSlot()) {
            AutomaticRefillingHandler.reset();
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed() && ConfigManager.PROFILES.is(Status.ENABLED)) {
            client.setScreen(new ProfilesScreen());
        }
    }

    private static void captureMainHand(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AutomaticRefillingHandler.runMainHand();
            if (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed()) {
                if (!(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && AutomaticRefillingHandler.TOOL_CLASSES.contains(InteractionHandler.getMainHandStack().getItem().getClass()) && InteractionHandler.getMainHandStack().getMaxDamage() - InteractionHandler.getMainHandStack().getDamage() == 1)) {
                    AutomaticRefillingHandler.setMainHandStack(InteractionHandler.getMainHandStack());
                }
            } else AutomaticRefillingHandler.reset();
        } else AutomaticRefillingHandler.reset();
    }

    private static void captureOffHand(MinecraftClient client) {
        if (client.player == null || client.player.isInCreativeMode()) return;
        if (client.currentScreen == null) {
            if (AutomaticRefillingHandler.RUN_OFFHAND) AutomaticRefillingHandler.runOffHand();
            else AutomaticRefillingHandler.RUN_OFFHAND = true;
            if (client.options.useKey.isPressed()) {
                if (!(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && AutomaticRefillingHandler.TOOL_CLASSES.contains(InteractionHandler.getOffHandStack().getItem().getClass()) && InteractionHandler.getOffHandStack().getMaxDamage() - InteractionHandler.getOffHandStack().getDamage() == 1)) {
                    AutomaticRefillingHandler.setOffHandStack(InteractionHandler.getOffHandStack());
                }
            }
        } else AutomaticRefillingHandler.reset();
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
            } else AutomaticRefillingHandler.reset();
        }
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

    private static void captureInventory(MinecraftClient client) {
        if (client.player != null) LockedSlotsHandler.setSavedInventory(client.player.getInventory());
    }
}
