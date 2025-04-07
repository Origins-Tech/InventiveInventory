package net.inventive_mods.inventive_inventory.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;


public class ComponentsHelper {

    public static boolean arePotionsEqual(ItemStack stack, ItemStack otherStack) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        PotionContentsComponent otherPotionContentsComponent = otherStack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent == null && otherPotionContentsComponent == null) return true;
        if (potionContentsComponent == null || otherPotionContentsComponent == null) return false;
        if (potionContentsComponent.potion().isEmpty() || otherPotionContentsComponent.potion().isEmpty()) return false;
        return potionContentsComponent.potion().get().getIdAsString().equals(otherPotionContentsComponent.potion().get().getIdAsString());
    }

    public static boolean areCustomNamesEqual(ItemStack stack, ItemStack otherStack) {
        return stack.getName().getString().equals(otherStack.getName().getString());
    }

    public static boolean areEnchantmentsEqual(ItemStack stack, ItemStack otherStack) {
        return EnchantmentHelper.getEnchantments(stack).equals(EnchantmentHelper.getEnchantments(otherStack));
    }
}
