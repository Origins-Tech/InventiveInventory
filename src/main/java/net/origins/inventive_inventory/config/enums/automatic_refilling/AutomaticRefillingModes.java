package net.origins.inventive_inventory.config.enums.automatic_refilling;

import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;

public enum AutomaticRefillingModes {
    AUTOMATIC,
    SEMI_AUTOMATIC;

    public boolean isValid() {
        return ConfigManager.AUTOMATIC_REFILLING_MODE.is(AutomaticRefillingModes.SEMI_AUTOMATIC) && AdvancedOperationHandler.isPressed() ||
                ConfigManager.AUTOMATIC_REFILLING_MODE.is(AutomaticRefillingModes.AUTOMATIC) && !AdvancedOperationHandler.isPressed();
    }
}