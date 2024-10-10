package net.origins.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;

import java.util.ArrayList;

public class PlayerSlots {
    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

        int start, stop;
        if (screenHandler instanceof PlayerScreenHandler) {
            start = PlayerScreenHandler.INVENTORY_START;
            stop = PlayerScreenHandler.HOTBAR_END;
        } else if (screenHandler instanceof CrafterScreenHandler) {
            start = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE - 1;
            stop = screenHandler.slots.size() - PlayerInventory.getHotbarSize() - 1;
        } else {
            start = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
            stop = screenHandler.slots.size() - PlayerInventory.getHotbarSize();
        }

        return new SlotRange(start, stop);
    }

    public static SlotRange get(SlotTypes... types) {
        SlotRange slotRange = SlotRange.of(new ArrayList<>());
        for (SlotTypes type : types) {
            if (type != SlotTypes.LOCKED_SLOT) slotRange.append(type);
            else throw new IllegalArgumentException("This SlotType is not valid in this function");
        }
        return slotRange;
    }
}
