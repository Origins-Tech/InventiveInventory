package net.origins.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;

public class ContainerSlots {
    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int start = 0;
        int stop = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
        return new SlotRange(start, stop);
    }
}
