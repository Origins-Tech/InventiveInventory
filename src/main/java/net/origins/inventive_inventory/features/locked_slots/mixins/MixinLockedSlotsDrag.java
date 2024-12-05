package net.origins.inventive_inventory.features.locked_slots.mixins;


import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class MixinLockedSlotsDrag {

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        if (ContextManager.isLockedSlots() && MouseLocation.getHoveredSlot() != null) {
            LockedSlotsHandler.dragToggle(MouseLocation.getHoveredSlot().id);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (ContextManager.isLockedSlots()) ContextManager.setContext(Contexts.INIT);
    }
}
