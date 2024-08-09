package net.origins.inventive_inventory.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.origins.inventive_inventory.InventiveInventory;

public class InteractionHandler {
    private static final int LEFT_CLICK = 0;
    private static final int RIGHT_CLICK = 1;

    public static ItemStack getCursorStack() {
        return InventiveInventory.getScreenHandler().getCursorStack();
    }

    public static boolean isCursorFull() {
        return !getCursorStack().isEmpty();
    }

    public static ItemStack getStackFromSlot(int slot) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        return screenHandler.getSlot(slot).getStack();
    }

    public static int getSelectedSlot() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        return screenHandler.getSlotIndex(player.getInventory(), player.getInventory().selectedSlot).orElse(-1);
    }

    public static void setSelectedSlot(int slot) {
        InventiveInventory.getPlayer().getInventory().selectedSlot = slot;
    }

    public static ItemStack getMainHandStack() {
        return InventiveInventory.getPlayer().getMainHandStack();
    }

    public static ItemStack getOffHandStack() {
        return InventiveInventory.getPlayer().getOffHandStack();
    }

    public static ItemStack getAnyHandStack() {
        if (!ItemStack.areItemsEqual(getMainHandStack(), ItemStack.EMPTY)) return getMainHandStack();
        else if (!ItemStack.areItemsEqual(getOffHandStack(), ItemStack.EMPTY)) return getOffHandStack();
        else return null;
    }

    public static void leftClickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void rightClickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, RIGHT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        manager.clickSlot(getSyncId(), target, LEFT_CLICK, SlotActionType.PICKUP, player);
        if (isCursorFull()) {
            manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        }
    }

    public static void dropStack(int slot) {
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        int selectedSlot = getSelectedSlot();
        ItemStack selectedStack = getStackFromSlot(selectedSlot).copy();
        ItemStack slotStack = getStackFromSlot(slot).copy();
        if (ItemStack.areItemsEqual(slotStack, selectedStack) && slotStack.getCount() + selectedStack.getCount() > slotStack.getMaxCount()) {
            leftClickStack(selectedSlot);
            while (getCursorStack().getCount() > slotStack.getCount()) rightClickStack(slot);
            leftClickStack(selectedSlot);
        } else swapStacks(slot, selectedSlot);
        player.dropSelectedItem(true);
        if (ItemStack.areItemsEqual(slotStack, selectedStack) && slotStack.getCount() + selectedStack.getCount() > slotStack.getMaxCount()) {
            swapStacks(slot, selectedSlot);
        } else swapStacks(selectedSlot, slot);
    }

    public static void dropItem(int slot, int times) {
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        int selectedSlot = getSelectedSlot();

        swapStacks(slot, selectedSlot);
        for (; times > 0; times--) player.dropSelectedItem(false);
        swapStacks(slot, selectedSlot);
    }

    private static int getSyncId() {
        return InventiveInventory.getScreenHandler().syncId;
    }
}
