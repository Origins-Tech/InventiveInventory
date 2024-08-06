package net.origins.inventive_inventory.util.slots;

import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.util.ScreenCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SlotRange extends ArrayList<Integer> {

    public SlotRange(int start, int stop) {
        super(IntStream.range(start, stop).boxed().toList());
    }

    private SlotRange(List<Integer> list) {
        super(list);
    }

    public static SlotRange of(List<Integer> list) {
        return new SlotRange(list);
    }

    public static boolean slotIn(SlotTypes type, int slot) {
        return PlayerSlots.get(type).contains(slot);
    }

    public SlotRange append(SlotTypes type) {
        if (type == SlotTypes.HOTBAR) {
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            int start = screenHandler.slots.size() - PlayerSlots.HOTBAR_SIZE - (screenHandler instanceof PlayerScreenHandler ? PlayerSlots.OFFHAND_SIZE : 0);
            int stop = screenHandler.slots.size() - (screenHandler instanceof PlayerScreenHandler ? PlayerSlots.OFFHAND_SIZE : 0);
            IntStream.range(start, stop).forEach(this::add);
        } else if (type == SlotTypes.INVENTORY) {
            this.addAll(PlayerSlots.get());
        } else if (type == SlotTypes.OFFHAND) {
            if (ScreenCheck.isPlayerHandler()) this.add(PlayerScreenHandler.OFFHAND_ID);
        }
        return this;
    }

    public SlotRange exclude(SlotTypes type) {
        if (type == SlotTypes.LOCKED_SLOT) LockedSlotsHandler.getLockedSlots().forEach(this::remove);
        else if (type == SlotTypes.INVENTORY) PlayerSlots.get().forEach(this::remove);
        else if (type == SlotTypes.HOTBAR) PlayerSlots.get(SlotTypes.HOTBAR).forEach(this::remove);
        return this;
    }

    public SlotRange exclude(Integer slot) {
        this.remove(slot);
        return this;
    }

    public SlotRange copy() {
        return (SlotRange) this.clone();
    }
}
