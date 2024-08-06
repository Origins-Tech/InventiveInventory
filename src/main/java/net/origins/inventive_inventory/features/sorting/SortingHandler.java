package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import net.origins.inventive_inventory.util.slots.ContainerSlots;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

public class SortingHandler {

    public static void sort() {
        ContextManager.setContext(Contexts.SORTING);
        if (InventiveInventory.getPlayer().isCreative() || ConfigManager.SORTING.is(Status.DISABLED)) return;
        SlotRange slotRange = MouseLocation.isOverInventory() ? PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT) : ContainerSlots.get();
        ItemStack targetStack = InteractionHandler.getCursorStack().copy();

        SortingHelper.mergeItemStacks(slotRange);
        SortingHelper.sortItemStacks(slotRange);

        if (ConfigManager.CURSOR_STACK_BEHAVIOUR.getValue().isValid())
            SortingHelper.adjustCursorStack(slotRange, targetStack);
        ContextManager.setContext(Contexts.INIT);
    }
}
