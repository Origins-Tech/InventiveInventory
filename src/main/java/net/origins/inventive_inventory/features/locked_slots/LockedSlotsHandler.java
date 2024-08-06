package net.origins.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;

import java.nio.file.Path;
import java.util.List;

public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);
    private static LockedSlots lockedSlots = new LockedSlots(List.of());

    public static void toggle(int slot) {
        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        if (PlayerSlots.get().contains(slot)) {
            lockedSlots = getLockedSlots();
            if (lockedSlots.contains(slot)) {
                lockedSlots.remove(((Integer) slot));
            } else lockedSlots.add(slot);
            lockedSlots = lockedSlots.unadjust();
            JsonArray lockedSlotsJson = new JsonArray();
            for (int lockedSlot : lockedSlots) {
                lockedSlotsJson.add(lockedSlot);
            }
            JsonObject jsonObject = FileHandler.get(LOCKED_SLOTS_PATH).isJsonObject() ? FileHandler.get(LOCKED_SLOTS_PATH).getAsJsonObject() : new JsonObject();
            jsonObject.remove(InventiveInventory.getWorldName());
            jsonObject.add(InventiveInventory.getWorldName(), lockedSlotsJson);
            FileHandler.write(LOCKED_SLOTS_PATH, jsonObject);
        }
        ContextManager.setContext(Contexts.INIT);
    }

    public static LockedSlots getLockedSlots() {
        if (lockedSlots.isEmpty()) {
            JsonElement jsonFile = FileHandler.get(LOCKED_SLOTS_PATH);
            JsonArray lockedSlotsJson = new JsonArray();
            if (jsonFile.isJsonObject() && jsonFile.getAsJsonObject().has(InventiveInventory.getWorldName())) {
                lockedSlotsJson = jsonFile.getAsJsonObject().getAsJsonArray(InventiveInventory.getWorldName());
            }
            for (JsonElement slot : lockedSlotsJson.getAsJsonArray()) {
                lockedSlots.add(slot.getAsInt());
            }
        }
        return lockedSlots.adjust();
    }

    public static void clearLockedSlots() {
        lockedSlots.clear();
    }
}
