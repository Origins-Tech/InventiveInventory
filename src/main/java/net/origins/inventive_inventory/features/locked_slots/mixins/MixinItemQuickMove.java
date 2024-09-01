package net.origins.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenHandler.class)
public class MixinItemQuickMove {

    @ModifyExpressionValue(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean isEmptyAndLockedSlot(boolean original, @Local(ordinal = 2) int i) {
        if (InventiveInventory.getClient().isInSingleplayer()) {
            return original || LockedSlotsHandler.getLockedSlots().contains(i);
        } return original;
    }

    @ModifyExpressionValue(method = "insertItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 3))
    private boolean notEmptyAndLockedSlot(boolean original, @Local(ordinal = 2) int i) {
        if (InventiveInventory.getClient().isInSingleplayer()) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(i);
        } return original;
    }

    @ModifyExpressionValue(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;hasStack()Z", ordinal = 3))
    private boolean preventPickupAll(boolean original, @Local(ordinal = 1) Slot slot) {
        if (InventiveInventory.getClient().isInSingleplayer()) {
            return original && !LockedSlotsHandler.getLockedSlots().contains(slot.id);
        } return original;
    }
}
