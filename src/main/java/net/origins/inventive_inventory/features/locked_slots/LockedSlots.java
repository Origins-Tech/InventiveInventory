package net.origins.inventive_inventory.features.locked_slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.ScreenCheck;

import java.util.ArrayList;
import java.util.List;

public class LockedSlots extends ArrayList<Integer> {

    public LockedSlots(List<Integer> list) {
        super(list);
    }

    public LockedSlots adjust() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int size = ScreenCheck.hasExtraSlot() ? screenHandler.slots.size() - 1 : screenHandler.slots.size();
        return new LockedSlots(this.stream().map(slot -> slot + size - PlayerInventory.MAIN_SIZE).toList());
    }

    public LockedSlots unadjust() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int size = ScreenCheck.hasExtraSlot() ? screenHandler.slots.size() - 1 : screenHandler.slots.size();
        return new LockedSlots(this.stream().map(slot -> slot - size + PlayerInventory.MAIN_SIZE).toList());
    }
}
