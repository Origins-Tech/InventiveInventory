package net.origins.inventive_inventory.config.enums.automatic_refilling;

import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;

public enum ToolReplacementPriority implements Translatable {
    MATERIAL("material"),
    DURABILITY("durability");

    private final String translationKey;

    ToolReplacementPriority(String translationKey) {
        this.translationKey = ConfigManager.OPTION_TRANSLATION_KEY + ".automatic_refilling.tool_replacement_priority." + translationKey;
    }

    @Override
    public Text getText() {
        return Text.translatable(this.translationKey);
    }
}

