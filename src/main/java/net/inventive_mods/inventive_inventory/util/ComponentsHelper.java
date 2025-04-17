package net.inventive_mods.inventive_inventory.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;


public class ComponentsHelper {

    public static boolean arePotionsEqual(ItemStack stack, ItemStack otherStack) {
        NbtCompound potionContentsComponent = stack.getSubNbt("Potion");
        NbtCompound otherPotionContentsComponent = otherStack.getSubNbt("Potion");
        if (potionContentsComponent == null && otherPotionContentsComponent == null) return true;
        if (potionContentsComponent == null || otherPotionContentsComponent == null) return false;
        if (potionContentsComponent.isEmpty() || otherPotionContentsComponent.isEmpty()) return false;
        return potionContentsComponent.equals(otherPotionContentsComponent);
    }

    public static boolean areCustomNamesEqual(ItemStack stack, ItemStack otherStack) {
        return stack.getName().getString().equals(otherStack.getName().getString());
    }

    public static boolean areEnchantmentsEqual(ItemStack stack, ItemStack otherStack) {
        return EnchantmentHelper.get(stack).equals(EnchantmentHelper.get(otherStack));
    }
}
