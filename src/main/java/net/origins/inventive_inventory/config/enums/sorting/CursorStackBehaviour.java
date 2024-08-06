package net.origins.inventive_inventory.config.enums.sorting;


import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.SpecialName;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;

public enum CursorStackBehaviour implements SpecialName {
    AOK_DEPENDENT("AOK-Dependent"),
    AOK_DEPENDENT_INVERTED("AOK-Dependent Inverted"),
    @SuppressWarnings("unused") SORT_CURSOR_STACK("Sort Cursor Stack"),
    KEEP_CURSOR_STACK("Keep Cursor Stack");

    private final String name;

    CursorStackBehaviour(String name) {
        this.name = name;
    }

    public boolean isValid() {
        return ConfigManager.CURSOR_STACK_BEHAVIOUR.is(KEEP_CURSOR_STACK) ||
                ConfigManager.CURSOR_STACK_BEHAVIOUR.is(AOK_DEPENDENT) && AdvancedOperationHandler.isPressed() ||
                ConfigManager.CURSOR_STACK_BEHAVIOUR.is(AOK_DEPENDENT_INVERTED) && !AdvancedOperationHandler.isPressed();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
