package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;

public class ConnectionEvents {

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> LockedSlotsHandler.initLockedSlots());
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ProfileHandler.initProfiles());
    }
}
