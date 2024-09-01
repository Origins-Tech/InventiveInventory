package net.origins.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
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
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(true)) return;
        List<ItemStack> currentInventory = new ArrayList<>(InventiveInventory.getPlayer().getInventory().main);
        if (savedInventory.isEmpty() || !pickedUpItem(currentInventory)) return;
        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        LockedSlots lockedSlots = LockedSlotsHandler.getLockedSlots();

        int i = 9;
        for (int invSlot : PlayerSlots.get()) {
            ItemStack currentStack = currentInventory.get(i);
            ItemStack savedStack = savedInventory.get(i);
            i++;
            if (!lockedSlots.contains(invSlot) || ItemStack.areEqual(currentStack, savedStack)) continue;
            List<Integer> suitableSlots = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(SlotTypes.LOCKED_SLOT).stream()
                    .filter(slot -> {
                        ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                        return stack.isEmpty() || ItemStack.areItemsEqual(stack, currentStack) && stack.getCount() < stack.getMaxCount();
                    })
                    .sorted(Comparator.comparing((Integer slot) -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder())
                            .thenComparing(slot -> slot))
                    .toList();
            if (!suitableSlots.isEmpty()) {
                InteractionHandler.leftClickStack(invSlot);
                for (int slot : suitableSlots) {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    while (InteractionHandler.getCursorStack().getCount() > savedStack.getCount()) {
                        if (stack.getCount() < stack.getMaxCount()) InteractionHandler.rightClickStack(slot);
                        else break;
                    }
                }
                InteractionHandler.leftClickStack(invSlot);
            }
            if (InteractionHandler.getStackFromSlot(invSlot).getCount() > savedStack.getCount()) {
                InteractionHandler.dropItem(invSlot, InteractionHandler.getStackFromSlot(invSlot).getCount() - savedStack.getCount());
            }
        }
        ContextManager.setContext(Contexts.INIT);
    }

    public static void setSavedInventory(PlayerInventory currentInventory) {
        savedInventory = new ArrayList<>(currentInventory.main);
        savedInventory.add(InteractionHandler.getCursorStack().copy());
    }

    private static boolean pickedUpItem(List<ItemStack> currentInventory) {
        List<Item> uniqueItems = currentInventory.stream().map(ItemStack::getItem).distinct().toList();
        for (Item item : uniqueItems) {
            int currentCount = 0;
            int savedCount = 0;
            for (int i = 0; i < savedInventory.size(); i++) {
                if (i < currentInventory.size() && ItemStack.areItemsEqual(item.getDefaultStack(), currentInventory.get(i))) {
                    currentCount += currentInventory.get(i).getCount();
                }
                if (ItemStack.areItemsEqual(item.getDefaultStack(), savedInventory.get(i))) {
                    savedCount += savedInventory.get(i).getCount();
                }
            }
            if (currentCount > savedCount) return true;
        } return false;
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
