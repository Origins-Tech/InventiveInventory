package net.inventive_mods.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.ConfigManager;
import net.inventive_mods.inventive_inventory.config.enums.locked_slots.Style;
import net.inventive_mods.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.inventive_mods.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.inventive_mods.inventive_inventory.util.Drawer;
import net.inventive_mods.inventive_inventory.util.Textures;
import net.inventive_mods.inventive_inventory.util.slots.PlayerSlots;
import net.inventive_mods.inventive_inventory.util.slots.SlotTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinLockedSlotsDrawer {

    @Shadow
    protected abstract void drawSlot(DrawContext context, Slot slot);

    @Inject(method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void onDrawItem(DrawContext context, Slot slot, CallbackInfo ci) {
        if (!InventiveInventory.getPlayer().isInCreativeMode() && LockedSlotsHandler.getLockedSlots().contains(slot.id) ) {
            Drawer.drawSlotBackground(context, slot.x, slot.y, ConfigManager.LOCKED_SLOTS_COLOR.getValue(), 0, ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, slot.x + 11, slot.y - 2, 200, 8);
        }
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V"))
    private void drawSlotHighlight(DrawContext context, int x, int y, int z, Operation<Void> original, @Local Slot slot) {
        if (!InventiveInventory.getPlayer().isInCreativeMode()) {
            if (AdvancedOperationHandler.isPressed()) {
                if (LockedSlotsHandler.getLockedSlots().contains(slot.id)) {
                    Drawer.drawSlotBackground(context, x, y, LockedSlotsHandler.LOCKED_HOVER_COLOR, z + 200, false);
                    this.drawSlot(context, slot);
                    return;
                } else if (PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(SlotTypes.LOCKED_SLOT).contains(slot.id)) {
                    Drawer.drawSlotBackground(context, x, y, LockedSlotsHandler.HOVER_COLOR, z + 1, false);
                    this.drawSlot(context, slot);
                    return;
                }
            } else if (LockedSlotsHandler.getLockedSlots().contains(slot.id)) {
                original.call(context, x, y, z);
                if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, slot.x + 11, slot.y - 2, 300, 8);
                return;
            }
        }
        original.call(context, x, y, z);
    }
}
