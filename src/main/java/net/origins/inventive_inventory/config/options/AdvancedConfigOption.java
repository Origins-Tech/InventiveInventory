package net.origins.inventive_inventory.config.options;

import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import org.jetbrains.annotations.Nullable;

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

    protected void cycle() {
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
            if (config.toString().equalsIgnoreCase(value)) {
                this.setValue(config);
                return;
            }
        }
    }

    @Override
    public CyclingButtonWidget<?> asButton() {
        return CyclingButtonWidget.builder(ConfigOption::getValueAsText)
                .tooltip(SimpleOption.constantTooltip(this.tooltip))
                .omitKeyText()
                .values(this.enumClass.getEnumConstants()).initially(this.getValue())
                .build(Text.empty(), (button, value) -> this.cycle());
    }
}
