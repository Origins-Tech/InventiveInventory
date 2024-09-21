package net.origins.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
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
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);
    private static final List<ItemStack> savedInventory = new ArrayList<>();
    private static final List<ItemStack> savedHandlerInventory = new ArrayList<>();
    public static boolean shouldAdd;
    private static LockedSlots lockedSlots = new LockedSlots(List.of());

    public static final int HOVER_COLOR = 0x66FF0000;
    public static final int LOCKED_HOVER_COLOR = 0xFF8B0000;

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

    public static void initLockedSlots() {
        lockedSlots.clear();
        JsonElement jsonFile = FileHandler.get(LOCKED_SLOTS_PATH);
        JsonArray lockedSlotsJson = new JsonArray();
        if (jsonFile.isJsonObject() && jsonFile.getAsJsonObject().has(InventiveInventory.getWorldName())) {
            lockedSlotsJson = jsonFile.getAsJsonObject().getAsJsonArray(InventiveInventory.getWorldName());
        }
        for (JsonElement slot : lockedSlotsJson.getAsJsonArray()) {
            lockedSlots.add(slot.getAsInt());
        }
    }

    public static LockedSlots getLockedSlots() {
        return lockedSlots.adjust();
    }

    public static void adjustInventory() {
        List<ItemStack> currentInventory = new ArrayList<>(InventiveInventory.getPlayer().getInventory().main);
        if (savedInventory.isEmpty()) return;
        boolean tookItem = tookItem(currentInventory);
        boolean movedItem = movedItem(tookItem);

        ContextManager.setContext(Contexts.LOCKED_SLOTS);
        if (ConfigManager.PICKUP_INTO_LOCKED_SLOTS.is(false) && tookItem && !movedItem) {
            rearrange(currentInventory, InteractionHandler::dropItem);
        } else if (ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS.is(false) && movedItem) {
            rearrange(currentInventory, (slot, times) -> InteractionHandler.quickMove(slot));
        }
        ContextManager.setContext(Contexts.INIT);
    }

    public static void setSavedInventory() {
        savedInventory.clear();
        for (ItemStack stack : InventiveInventory.getPlayer().getInventory().main) savedInventory.add(stack.copy());
        savedInventory.add(InteractionHandler.getCursorStack().copy());
    }

    public static void setSavedHandlerInventory() {
        savedHandlerInventory.clear();
        if (!ScreenCheck.isNone()) {
            for (int i = 0; i < InventiveInventory.getScreenHandler().slots.size(); i++) {
                savedHandlerInventory.add(InteractionHandler.getStackFromSlot(i).copy());
            }
        }
    }

    private static boolean tookItem(List<ItemStack> currentInventory) {
        Map<Item, Integer> currentCountMap = currentInventory.stream()
                .collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum));
        Map<Item, Integer> savedCountMap = savedInventory.stream()
                .collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum));
        return currentCountMap.entrySet().stream()
                .anyMatch(entry -> entry.getValue() > savedCountMap.getOrDefault(entry.getKey(), 0));
    }

    private static boolean movedItem(boolean tookItem) {
        List<ItemStack> currentHandlerInventory = new ArrayList<>();
        for (int i = 0; i < InventiveInventory.getScreenHandler().slots.size(); i++) {
            currentHandlerInventory.add(InteractionHandler.getStackFromSlot(i).copy());
        }

        Map<Item, Integer> currentCountMap = currentHandlerInventory.stream()
                .collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum));
        Map<Item, Integer> savedCountMap = savedHandlerInventory.stream()
                .collect(Collectors.toMap(ItemStack::getItem, ItemStack::getCount, Integer::sum));
        return tookItem && currentCountMap.entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(savedCountMap.getOrDefault(entry.getKey(), 0)));
    }

    private static void rearrange(List<ItemStack> currentInventory, BiConsumer<Integer, Integer> func) {
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
                func.accept(invSlot, InteractionHandler.getStackFromSlot(invSlot).getCount() - savedStack.getCount());
            }
        }
    }

    private static void save() {
        lockedSlots = lockedSlots.unadjust();
        JsonArray lockedSlotsJson = new JsonArray();
        for (int lockedSlot : lockedSlots) lockedSlotsJson.add(lockedSlot);
        JsonObject jsonObject = FileHandler.get(LOCKED_SLOTS_PATH).isJsonObject() ? FileHandler.get(LOCKED_SLOTS_PATH).getAsJsonObject() : new JsonObject();
        jsonObject.remove(InventiveInventory.getWorldName());
        jsonObject.add(InventiveInventory.getWorldName(), lockedSlotsJson);
        FileHandler.write(LOCKED_SLOTS_PATH, jsonObject);
    }
}
