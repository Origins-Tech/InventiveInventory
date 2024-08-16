package net.origins.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.ScreenCheck;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);
    private static LockedSlots lockedSlots = new LockedSlots(List.of());
    private static List<ItemStack> savedInventory = new ArrayList<>();
    public static boolean shouldAdd;

    public static void toggle(int slot) {
        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        if (PlayerSlots.get().contains(slot)) {
            lockedSlots = getLockedSlots();
            if (lockedSlots.contains(slot)) {
                lockedSlots.remove(((Integer) slot));
                shouldAdd = false;
            } else {
                lockedSlots.add(slot);
                shouldAdd = true;
            }
            save();
        }
    }

    public static void dragToggle(int slot) {
        if (PlayerSlots.get().contains(slot)) {
            lockedSlots = getLockedSlots();
            if (shouldAdd) {
                if (!lockedSlots.contains(slot)) lockedSlots.add(slot);
            } else lockedSlots.remove(Integer.valueOf(slot));
            save();
        }
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

    public static void adjustInventory() {
        List<ItemStack> currentInventory = InventiveInventory.getPlayer().getInventory().main.stream().toList();
        if (savedInventory.isEmpty() || savedInventory.equals(currentInventory) || ScreenCheck.isPlayerInventory()) return;
        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        LockedSlots lockedSlots = LockedSlotsHandler.getLockedSlots();

        for (int i = 9; i < currentInventory.size(); i++) {
            ItemStack currentStack = currentInventory.get(i);
            ItemStack savedStack = savedInventory.get(i);
            if (!lockedSlots.contains(i) || ItemStack.areEqual(currentStack, savedStack)) continue;
            List<Integer> suitableSlots = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(SlotTypes.LOCKED_SLOT).stream()
                    .filter(slot -> {
                        ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                        return stack.isEmpty() || ItemStack.areItemsEqual(stack, currentStack) && stack.getCount() < stack.getMaxCount();
                    })
                    .sorted(Comparator.comparing((Integer slot) -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder())
                            .thenComparing(slot -> slot))
                    .toList();
            if (!suitableSlots.isEmpty()) {
                InteractionHandler.leftClickStack(i);
                for (int slot : suitableSlots) {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    while (InteractionHandler.getCursorStack().getCount() > savedStack.getCount()) {
                        if (stack.getCount() < stack.getMaxCount()) InteractionHandler.rightClickStack(slot);
                        else break;
                    }
                }
                InteractionHandler.leftClickStack(i);
            }
            if (InteractionHandler.getStackFromSlot(i).getCount() > savedStack.getCount()) {
                InteractionHandler.dropItem(i, InteractionHandler.getStackFromSlot(i).getCount() - savedStack.getCount());
            }
        }
        ContextManager.setContext(Contexts.INIT);
    }

    public static void setSavedInventory(PlayerInventory currentInventory) {
        savedInventory = new ArrayList<>(currentInventory.main);
    }

    private static void save() {
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
}
