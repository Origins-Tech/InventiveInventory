package net.origins.inventive_inventory.config.enums.automatic_refilling;

import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;

public enum AutomaticRefillingModes implements Translatable {
    AUTOMATIC("automatic"),
    SEMI_AUTOMATIC("semi_automatic");

    private final String translationKey;

    AutomaticRefillingModes(String translationKey) {
        this.translationKey = "config.option_button.inventive_inventory.mode.automatic_refilling." + translationKey;
    }

    public boolean isValid() {
        return ConfigManager.AUTOMATIC_REFILLING_MODE.is(AutomaticRefillingModes.SEMI_AUTOMATIC) && AdvancedOperationHandler.isPressed() ||
                ConfigManager.AUTOMATIC_REFILLING_MODE.is(AutomaticRefillingModes.AUTOMATIC) && !AdvancedOperationHandler.isPressed();
    }

    @Override
    public Text getText() {
        return Text.translatable(this.translationKey);
    }
}