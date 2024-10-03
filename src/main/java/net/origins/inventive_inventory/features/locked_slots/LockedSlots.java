package net.origins.inventive_inventory.features.locked_slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.integrations.TrinketsIntegration;
import net.origins.inventive_inventory.util.ScreenCheck;

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
        int size = screenHandler.slots.size();
        if (ScreenCheck.isPlayerHandler()) size -= TrinketsIntegration.getSlotCount() + 1;
        else if (screenHandler instanceof CrafterScreenHandler) size -= 1;
        return size;
    }
}
