package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;

public class ConnectionEvents {

    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> LockedSlotsHandler.clearLockedSlots());
    }
}
