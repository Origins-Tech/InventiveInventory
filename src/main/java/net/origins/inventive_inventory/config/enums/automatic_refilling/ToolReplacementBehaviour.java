package net.origins.inventive_inventory.config.enums.automatic_refilling;

import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;

public enum ToolReplacementBehaviour implements Translatable {
    KEEP_TOOL("keep_tool"),
    BREAK_TOOL("break_tool");

    private final String translationKey;

    ToolReplacementBehaviour(String translationKey) {
        this.translationKey = "automatic_refilling.tool_replacement_behaviour." + translationKey;
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
