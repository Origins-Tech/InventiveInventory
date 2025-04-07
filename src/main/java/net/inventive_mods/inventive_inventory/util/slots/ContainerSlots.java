package net.inventive_mods.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.inventive_mods.inventive_inventory.InventiveInventory;

import java.util.List;

public class ContainerSlots {
    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<Slot> containerSlots = screenHandler.slots.stream().filter(slot -> !(slot.inventory instanceof PlayerInventory)).toList();
        if (containerSlots.isEmpty()) return new SlotRange(0, 0);
        return new SlotRange(containerSlots.getFirst().id, containerSlots.getLast().id);
    }
}
