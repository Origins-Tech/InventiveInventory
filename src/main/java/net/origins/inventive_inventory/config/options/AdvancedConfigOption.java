package net.origins.inventive_inventory.config.options;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import net.origins.inventive_inventory.config.enums.accessors.Translatable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class AdvancedConfigOption<E extends Enum<E>> extends ConfigOption<E> {
    private final Class<E> enumClass;
    private final Text tooltip;

    public AdvancedConfigOption(String key, E defaultValue, ConfigType configType) {
        this(key, key, defaultValue, configType);
    }

    public AdvancedConfigOption(String key, String tooltipKey, E defaultValue, ConfigType configType) {
        super(key, defaultValue, configType);
        this.enumClass = defaultValue.getDeclaringClass();
        this.tooltip = Text.translatable("optionTooltip." + InventiveInventory.MOD_ID + "." + tooltipKey);
    }

    private void cycle() {
        E[] values = enumClass.getEnumConstants();
        this.setValue(values[(this.getValue().ordinal() + 1) % values.length]);
    }

    @Override
    public E[] getValues() {
        return enumClass.getEnumConstants();
    }

    @Override
    public void setValue(@Nullable String value) {
        for (E config : enumClass.getEnumConstants()) {
            if (config instanceof Translatable) {
                if (((Translatable) config).getText().getString().equalsIgnoreCase(value)) {
                    this.setValue(config);
                    return;
                }
            } else if (config.toString().equalsIgnoreCase(value)) {
                this.setValue(config);
                return;
            }
        }
    }

    @Override
    public SimpleOption<?> asButton() {
        return new SimpleOption<>(
                this.getTranslationKey(),
                SimpleOption.constantTooltip(this.tooltip),
                (text, value) -> getValueAsText(value),
                new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(enumClass.getEnumConstants()),
                        Codec.STRING.xmap(
                                string -> Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.name().toLowerCase().equals(string)).findAny().orElse(null),
                                newValue -> newValue.name().toLowerCase()
                        )),
                this.getValue(),
                e -> this.cycle());
    }
}
