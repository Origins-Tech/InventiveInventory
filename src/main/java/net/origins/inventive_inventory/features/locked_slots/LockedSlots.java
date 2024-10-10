package net.origins.inventive_inventory.features.locked_slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;

import java.util.ArrayList;
import java.util.List;

public class LockedSlots extends ArrayList<Integer> {

    public LockedSlots(List<Integer> list) {
        super(list);
    }

    public LockedSlots adjust() {
        return new LockedSlots(this.stream().map(slot -> slot + calculateSize() - PlayerInventory.MAIN_SIZE).toList());
    }

    public LockedSlots unadjust() {
        return new LockedSlots(this.stream().map(slot -> slot - calculateSize() + PlayerInventory.MAIN_SIZE).toList());
    }

    private int calculateSize() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int size;
        if (screenHandler instanceof PlayerScreenHandler) size = PlayerScreenHandler.HOTBAR_END;
        else if (screenHandler instanceof CrafterScreenHandler) size = screenHandler.slots.size() - 1;
        else size = screenHandler.slots.size();
        return size;
    }
}
