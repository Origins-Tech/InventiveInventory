package net.origins.inventive_inventory.config.options;

import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.accessors.SpecialName;
import net.origins.inventive_inventory.config.enums.accessors.Stylable;
import org.jetbrains.annotations.Nullable;


public abstract class ConfigOption<T> {
    private final String key;
    private final ConfigType configType;
    private T value;

    public static Text getValueAsText(Object value) {
        if (value instanceof Boolean) return (Boolean) value ? Text.of("Yes") : Text.of("No");
        else if (value instanceof Enum<?>) {
            if (value instanceof SpecialName) return Text.of(((SpecialName) value).getName());
            String[] words = value.toString().toLowerCase().split("_");
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
            Text text = Text.of(result.toString().trim());
            if (value instanceof Stylable) return text.copy().setStyle(((Stylable) value).getStyle());
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
        return "configOption." + InventiveInventory.MOD_ID + "." + this.key;
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
