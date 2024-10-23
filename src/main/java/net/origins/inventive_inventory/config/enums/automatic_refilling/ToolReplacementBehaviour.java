package net.origins.inventive_inventory.config.enums.automatic_refilling;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;

public enum ToolReplacementBehaviour implements Translatable {
    KEEP_TOOL("keep_tool"),
    BREAK_TOOL("break_tool");

    private final String translationKey;

    ToolReplacementBehaviour(String translationKey) {
        this.translationKey = "config.option_button.inventive_inventory.automatic_refilling.tool_replacement_behaviour." + translationKey;
    }

    public static boolean isValid(ItemStack stack) {
        return ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(KEEP_TOOL) && stack.getMaxDamage() - stack.getDamage() == 2 ||
                ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.is(BREAK_TOOL) && stack.getMaxDamage() - stack.getDamage() == 0;
    }

    @Override
    public Text getText() {
        return Text.translatable(this.translationKey);
    }
}
