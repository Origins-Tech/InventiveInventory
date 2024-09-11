package net.origins.inventive_inventory.features.automatic_refilling;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.config.enums.automatic_refilling.ToolReplacementBehaviour;
import net.origins.inventive_inventory.config.enums.automatic_refilling.ToolReplacementPriority;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class AutomaticRefillingHandler {

    public static final List<Class<? extends Item>> TOOL_CLASSES = List.of(SwordItem.class, PickaxeItem.class, AxeItem.class, ShovelItem.class, HoeItem.class, BowItem.class, CrossbowItem.class, TridentItem.class, MaceItem.class);
    private static final List<Item> EMPTIES = List.of(Items.BUCKET, Items.GLASS_BOTTLE, Items.BOWL);
    public static int SELECTED_SLOT;
    public static boolean RUN_OFFHAND = true;
    private static ItemStack offHandStack = ItemStack.EMPTY;
    private static ItemStack mainHandStack = ItemStack.EMPTY;

    public static void setOffHandStack(ItemStack itemStack) {
        offHandStack = itemStack.copy();
    }

    public static void setMainHandStack(ItemStack itemStack) {
        mainHandStack = itemStack.copy();
    }

    public static void runMainHand() {
        SELECTED_SLOT = InteractionHandler.getSelectedSlot();
        boolean isAutomaticRefillingDisabled = ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.DISABLED);
        boolean capturedStackIsEmpty = ItemStack.areItemsEqual(mainHandStack, ItemStack.EMPTY);
        ItemStack currentStack = InteractionHandler.getMainHandStack();
        boolean handFullAndNoTool = ItemStack.areItemsEqual(mainHandStack, currentStack) && !TOOL_CLASSES.contains(currentStack.getItem().getClass());
        boolean handFullAndToolDurabilityOver1 = ItemStack.areItemsEqual(mainHandStack, currentStack) && TOOL_CLASSES.contains(currentStack.getItem().getClass()) && currentStack.getMaxDamage() - currentStack.getDamage() > 1;
        if (isAutomaticRefillingDisabled || capturedStackIsEmpty || handFullAndNoTool || handFullAndToolDurabilityOver1)
            return;
        if (ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL) && TOOL_CLASSES.contains(currentStack.getItem().getClass()) && currentStack.getMaxDamage() - currentStack.getDamage() == 1)
            return;

        List<Integer> sameItemSlots = getSameItemSlots(mainHandStack);

        int emptiesSlot = InteractionHandler.getSelectedSlot();
        if (!sameItemSlots.isEmpty()) {
            if (SlotRange.slotIn(SlotTypes.HOTBAR, sameItemSlots.getFirst()))
                InteractionHandler.setSelectedSlot(sameItemSlots.getFirst() - PlayerInventory.MAIN_SIZE);
            else {
                InteractionHandler.swapStacks(sameItemSlots.getFirst(), InteractionHandler.getSelectedSlot());
                emptiesSlot = sameItemSlots.getFirst();
            }
            RUN_OFFHAND = false;
            offHandStack = ItemStack.EMPTY;
        }

        mainHandStack = ItemStack.EMPTY;
        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    public static void runOffHand() {
        boolean isAutomaticRefillingDisabled = ConfigManager.AUTOMATIC_REFILLING_STATUS.is(Status.DISABLED);
        boolean capturedStackIsEmpty = ItemStack.areItemsEqual(offHandStack, ItemStack.EMPTY);
        ItemStack currentStack = InteractionHandler.getOffHandStack();
        boolean handFullAndNoTool = ItemStack.areItemsEqual(offHandStack, currentStack) && !TOOL_CLASSES.contains(currentStack.getItem().getClass());
        boolean handFullAndToolDurabilityOver1 = ItemStack.areItemsEqual(offHandStack, currentStack) && TOOL_CLASSES.contains(currentStack.getItem().getClass()) && currentStack.getMaxDamage() - currentStack.getDamage() > 1;
        if (isAutomaticRefillingDisabled || capturedStackIsEmpty || handFullAndNoTool || handFullAndToolDurabilityOver1)
            return;
        if (ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL) && TOOL_CLASSES.contains(currentStack.getItem().getClass()) && currentStack.getMaxDamage() - currentStack.getDamage() == 1)
            return;
        SELECTED_SLOT = InteractionHandler.getSelectedSlot();

        List<Integer> sameItemSlots = getSameItemSlots(offHandStack);

        int emptiesSlot = PlayerScreenHandler.OFFHAND_ID;
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.swapStacks(sameItemSlots.getFirst(), PlayerScreenHandler.OFFHAND_ID);
            emptiesSlot = sameItemSlots.getFirst();
        }

        offHandStack = ItemStack.EMPTY;
        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    public static void reset() {
        setMainHandStack(ItemStack.EMPTY);
        setOffHandStack(ItemStack.EMPTY);
        RUN_OFFHAND = true;
        SELECTED_SLOT = -1;
    }

    private static List<Integer> getSameItemSlots(ItemStack handStack) {
        SlotRange slotRange = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(InteractionHandler.getSelectedSlot());
        slotRange = ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        Stream<Integer> sameItemSlotsStream = slotRange.stream()
                .filter(slot -> {
                    ItemStack itemStack = InteractionHandler.getStackFromSlot(slot);
                    boolean isEqualAndNoTool = ItemStack.areItemsAndComponentsEqual(handStack, itemStack) && !TOOL_CLASSES.contains(itemStack.getItem().getClass());
                    boolean isSameToolType = itemStack.getItem().getClass().equals(handStack.getItem().getClass()) && TOOL_CLASSES.contains(itemStack.getItem().getClass());
                    return isEqualAndNoTool || isSameToolType && ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.KEEP_TOOL) && itemStack.getMaxDamage() - itemStack.getDamage() > 1 || isSameToolType && ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(ToolReplacementBehaviour.BREAK_TOOL);
                });

        if (TOOL_CLASSES.contains(handStack.getItem().getClass())) {
            if (ConfigManager.TOOL_REPLACEMENT_PRIORITY.is(ToolReplacementPriority.MATERIAL)) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage(), Comparator.reverseOrder()));
            } else if (ConfigManager.TOOL_REPLACEMENT_PRIORITY.is(ToolReplacementPriority.DURABILITY)) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage() - InteractionHandler.getStackFromSlot(slot).getDamage()));
            }
        } else {
            sameItemSlotsStream = sameItemSlotsStream
                    .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()));
        }

        List<Integer> sameItemSlots = new ArrayList<>(sameItemSlotsStream.toList());
        SlotRange hotbarSlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.INVENTORY);
        SlotRange inventorySlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.HOTBAR);
        sameItemSlots.removeAll(!hotbarSlotRange.isEmpty() ? inventorySlotRange : hotbarSlotRange);
        return sameItemSlots;
    }

    private static void mergeEmpties(int itemSlot) {
        SlotRange slotRange = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(itemSlot);
        slotRange = ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.is(true) ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        List<Integer> sameItemSlots = slotRange.stream()
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getItem().equals(InteractionHandler.getStackFromSlot(itemSlot).getItem()))
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getCount() < InteractionHandler.getStackFromSlot(slot).getMaxCount())
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                .toList();
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.leftClickStack(itemSlot);
            InteractionHandler.leftClickStack(sameItemSlots.getFirst());
        }
    }
}
