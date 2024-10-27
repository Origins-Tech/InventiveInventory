package net.origins.inventive_inventory.util.slots;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.InventiveInventory;

import java.util.List;

public class ContainerSlots {
    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<Slot> containerSlots = screenHandler.slots.stream().filter(slot -> slot.inventory == null).toList();
        if (containerSlots.isEmpty()) return new SlotRange(0, 0);
        return new SlotRange(containerSlots.getFirst().id, containerSlots.getLast().id);
    }
}
