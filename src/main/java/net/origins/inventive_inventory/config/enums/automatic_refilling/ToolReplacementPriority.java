package net.origins.inventive_inventory.config.enums.automatic_refilling;

import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;

public enum ToolReplacementPriority implements Translatable {
    MATERIAL("material"),
    REMAINING_HEALTH("health");

    private final String translationKey;

    ToolReplacementPriority(String translationKey) {
        this.translationKey = "configOptionButton.inventive_inventory.automatic_refilling.tool_replacement_priority." + translationKey;
    }

    @Override
    public Text getText() {
        return Text.translatable(this.translationKey);
    }
}

