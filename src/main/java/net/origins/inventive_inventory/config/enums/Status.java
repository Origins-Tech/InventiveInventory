package net.origins.inventive_inventory.config.enums;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.config.enums.accessors.Stylable;

public enum Status implements Stylable {
    ENABLED(Formatting.GREEN),
    DISABLED(Formatting.RED);

    private final Style style;

    Status(Formatting color) {
        this.style = Style.EMPTY.withColor(color);
    }

    public Style getStyle() {
        return this.style;
    }

}
