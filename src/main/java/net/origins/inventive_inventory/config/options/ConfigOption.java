package net.origins.inventive_inventory.config.options;

import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.Stylable;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;
import org.jetbrains.annotations.Nullable;


public abstract class ConfigOption<T> {
    protected static final String simpleConfigOptionTranslationKey = "config.option_button." + InventiveInventory.MOD_ID + ".simple_config_option.";
    private final String key;
    private final ConfigType configType;
    private T value;

    public static Text getValueAsText(Object value) {
        if (value instanceof Boolean) return (Boolean) value ? Text.translatable(simpleConfigOptionTranslationKey + "yes") : Text.translatable(simpleConfigOptionTranslationKey + "no");
        else if (value instanceof Enum<?>) {
            Text text = Text.empty();
            if (value instanceof Translatable) text = ((Translatable) value).getText();
            if (value instanceof Stylable) text = text.copy().setStyle(((Stylable) value).getStyle());
            return text;
        } else return Text.empty();
    }

    public ConfigOption(String key, T defaultValue, ConfigType configType) {
        this.key = key;
        this.value = defaultValue;
        this.configType = configType;
    }

    public boolean is(T value) {
        return this.value == value;
    }

    public ConfigType getConfigType() {
        return this.configType;
    }

    public String getTranslationKey() {
        return "config.option." + InventiveInventory.MOD_ID + "." + this.key;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
        ConfigManager.save();
    }

    public abstract void setValue(@Nullable String value);

    public abstract T[] getValues();

    public abstract CyclingButtonWidget<?> asButton();
}
