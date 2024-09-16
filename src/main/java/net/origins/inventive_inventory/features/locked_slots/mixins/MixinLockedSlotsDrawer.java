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
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.locked_slots.Style;
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
            Drawer.drawSlotBackground(context, slot.x, slot.y, ConfigManager.LOCKED_SLOTS_COLOR.getValue(), 0, ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, slot.x + 11, slot.y - 2, 200, 8);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V"))
    private void drawSlotHighlight(DrawContext context, int x, int y, int z, Operation<Void> original, @Local Slot slot) {
        if (!InventiveInventory.getPlayer().isInCreativeMode()) {
            if (LockedSlotsHandler.getLockedSlots().contains(slot.id)) {
                if (AdvancedOperationHandler.isPressed()) {
                    Drawer.drawSlotBackground(context, x, y, LockedSlotsHandler.LOCKED_HOVER_COLOR, z + 200, false);
                    this.drawSlot(context, slot);
                } else {
                    original.call(context, x, y, z);
                    if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, slot.x + 11, slot.y - 2, 300, 8);
                }
            } else if (PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT).contains(slot.id) && AdvancedOperationHandler.isPressed()) {
                Drawer.drawSlotBackground(context, x, y, LockedSlotsHandler.HOVER_COLOR, z + 1, false);
                this.drawSlot(context, slot);
            } else original.call(context, x, y, z);
        } else original.call(context, x, y, z);
    }
}
