package net.origins.inventive_inventory.config.enums;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Stylable;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;

public enum Status implements Stylable, Translatable {
    ENABLED("enabled", Formatting.GREEN),
    DISABLED("disabled", Formatting.RED);

    private final String translationKey;
    private final Style style;

    Status(String translationKey, Formatting color) {
        this.translationKey = ConfigManager.OPTION_TRANSLATION_KEY + ".universal.status" + translationKey;
        this.style = Style.EMPTY.withColor(color);
    }

    public Style getStyle() {
        return this.style;
    }

    @Override
    public Text getText() {
        return Text.translatable(this.translationKey);
    }
}
