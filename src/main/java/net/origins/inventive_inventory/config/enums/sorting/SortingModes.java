package net.origins.inventive_inventory.config.enums.sorting;

import net.minecraft.item.Item;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.Comparator;

public enum SortingModes {
    NAME(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getName().getString())),
    @SuppressWarnings("unused") ITEM_TYPE(Comparator.comparing(slot -> Item.getRawId(InteractionHandler.getStackFromSlot(slot).getItem())));

    private final Comparator<Integer> comparator;

    SortingModes(Comparator<Integer> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Integer> getComparator() {
        return this.comparator.thenComparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder());
    }
}
