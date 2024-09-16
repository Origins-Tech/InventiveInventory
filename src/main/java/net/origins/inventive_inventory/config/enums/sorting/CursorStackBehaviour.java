package net.origins.inventive_inventory.config.enums.sorting;


import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;

public enum CursorStackBehaviour implements Translatable {
    AOK_DEPENDENT("aok_dependent"),
    AOK_DEPENDENT_INVERTED("aok_dependent_inverted"),
    @SuppressWarnings("unused") SORT_CURSOR_STACK("sort_cursor_stack"),
    KEEP_CURSOR_STACK("keep_cursor_stack");

    private final String translationKey;

    CursorStackBehaviour(String translationKey) {
        this.translationKey = "sorting.cursor_stack_behaviour." + translationKey;
    }

    public boolean isValid() {
        return ConfigManager.CURSOR_STACK_BEHAVIOUR.is(KEEP_CURSOR_STACK) ||
                ConfigManager.CURSOR_STACK_BEHAVIOUR.is(AOK_DEPENDENT) && AdvancedOperationHandler.isPressed() ||
                ConfigManager.CURSOR_STACK_BEHAVIOUR.is(AOK_DEPENDENT_INVERTED) && !AdvancedOperationHandler.isPressed();
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
