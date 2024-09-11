package net.origins.inventive_inventory.config.enums.sorting;

import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.Comparator;

public enum SortingModes implements Translatable {
    NAME("name", Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getName().getString())),
    @SuppressWarnings("unused") ITEM_TYPE("item_type", Comparator.comparing(slot -> Item.getRawId(InteractionHandler.getStackFromSlot(slot).getItem())));

    private final String translationKey;
    private final Comparator<Integer> comparator;

    SortingModes(String translationKey, Comparator<Integer> comparator) {
        this.translationKey = ConfigManager.OPTION_TRANSLATION_KEY +  ".sorting.mode.name" + translationKey;
        this.comparator = comparator;
    }

    public Comparator<Integer> getComparator() {
        return this.comparator.thenComparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder());
    }

    @Override
    public Text getText() {
        return Text.translatable(this.translationKey);
    }
}
