package net.origins.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.Drawer;
import net.origins.inventive_inventory.util.Textures;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class MixinLockedSlotsDrawer {

    @Shadow
    protected abstract void drawSlot(DrawContext context, Slot slot);

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void onDrawItem(DrawContext context, Slot slot, CallbackInfo ci) {
        if (LockedSlotsHandler.getLockedSlots().contains(slot.id) && !InventiveInventory.getPlayer().isInCreativeMode()) {
            Drawer.drawSlotBackground(context, slot.x, slot.y, 0xFF4D4D4D, 0);
            Drawer.drawTexture(context, Textures.LOCK, slot.x + 11, slot.y - 2, 200, 8);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V"))
    private void drawSlotHighlight(DrawContext context, int x, int y, int z, Operation<Void> original, @Local Slot slot) {
        if (!InventiveInventory.getPlayer().isInCreativeMode()) {
            if (LockedSlotsHandler.getLockedSlots().contains(slot.id)) {
                if (AdvancedOperationHandler.isPressed()) {
                    Drawer.drawSlotBackground(context, x, y, 0xFF8B0000, z + 200);
                    this.drawSlot(context, slot);
                } else {
                    original.call(context, x, y, z);
                    Drawer.drawTexture(context, Textures.LOCK, slot.x + 11, slot.y - 2, 300, 8);
                }
            } else if (PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT).contains(slot.id) && AdvancedOperationHandler.isPressed()) {
                Drawer.drawSlotBackground(context, x, y, 0x66FF0000, z + 1);
                this.drawSlot(context, slot);
            } else original.call(context, x, y, z);
        } else original.call(context, x, y, z);
    }
}
