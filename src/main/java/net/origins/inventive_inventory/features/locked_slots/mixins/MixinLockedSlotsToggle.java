package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinLockedSlotsToggle {

    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    private void toggleLockedSlots(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (AdvancedOperationHandler.isPressed() && button == 0 && actionType == SlotActionType.PICKUP && ContextManager.isInit()) {
            if (!InventiveInventory.getPlayer().isInCreativeMode()) {
                LockedSlotsHandler.toggle(slotId);
                ci.cancel();
            }
        }
    }
}

