package net.origins.inventive_inventory.config.enums.sorting;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

public enum SortingMode implements Translatable {
    NAME("name", Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getName().getString())),
    @SuppressWarnings("unused") ITEM_TYPE("item_type", Comparator.comparing(slot -> Item.getRawId(InteractionHandler.getStackFromSlot(slot).getItem())));

    private final String translationKey;
    private final Comparator<Integer> comparator;

    SortingMode(String translationKey, Comparator<Integer> comparator) {
        this.translationKey = "sorting.mode." + translationKey;
        this.comparator = comparator;
    }

    public Comparator<Integer> getComparator() {
        return this.comparator
                .thenComparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder())
                .thenComparing(slot -> {
                    ItemStack stack = InteractionHandler.getStackFromSlot(slot);
                    NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(stack);
                    if (nbtList.isEmpty()) nbtList = stack.getEnchantments();
                    if (!nbtList.isEmpty()) {
                        AtomicReference<String> result = new AtomicReference<>("");
                        EnchantmentHelper.fromNbt(nbtList).forEach((enchantment, integer) -> result.set(result.get().concat(enchantment.getName(0).getString() + " " + integer + ", ")));
                        return result.get();
                    }
                    return "";
                });
    }

    @Override
    public Text getButtonText() {
        return Text.translatable(ConfigManager.OPTION_TRANSLATION_KEY + "." + this.translationKey);
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }
}
