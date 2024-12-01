package net.origins.inventive_inventory.features.locked_slots.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerInventory;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.locked_slots.Style;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.util.Drawer;
import net.origins.inventive_inventory.util.Textures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinLockedSlotsInGameHudDrawer {

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"))
    private void onRenderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 4) int hotbarSlot, @Local(ordinal = 5) int x, @Local(ordinal = 6) int y) {
        if (hotbarSlot > 8) return;
        if (!InventiveInventory.getPlayer().isInCreativeMode() && LockedSlotsHandler.getLockedSlots().contains(hotbarSlot + PlayerInventory.MAIN_SIZE)) {
            Drawer.drawSlotBackground(context, x, y, ConfigManager.LOCKED_SLOTS_COLOR.getValue(), 0, ConfigManager.LOCKED_SLOT_STYLE.is(Style.OUTLINED));
            if (ConfigManager.SHOW_LOCK.is(true)) Drawer.drawTexture(context, Textures.LOCK, x + 11, y - 2, 8);
        }
    }
}

