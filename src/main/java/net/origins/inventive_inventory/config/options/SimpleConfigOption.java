package net.origins.inventive_inventory.config.options;

import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleConfigOption extends ConfigOption<Boolean> {
    private final Text tooltip;

    public SimpleConfigOption(String key, boolean defaultValue, ConfigType configType) {
        this(key, key, defaultValue, configType);
    }

    public SimpleConfigOption(String key, String tooltipKey, boolean defaultValue, ConfigType configType) {
        super(key, defaultValue, configType);
        this.tooltip = Text.translatable("optionTooltip." + InventiveInventory.MOD_ID + "." + tooltipKey);
    }

    protected void cycle() {
        this.setValue(!this.getValue());
    }

    @Override
    public Boolean[] getValues() {
        return new Boolean[]{true, false};
    }

    @Override
    public void setValue(@Nullable String value) {
        if (value != null) {
            if (value.equals("true") || value.equals(Text.translatable(simpleConfigOptionTranslationKey + "yes").getString())) this.setValue(true);
            else if (value.equals("false") || value.equals(Text.translatable(simpleConfigOptionTranslationKey + "no").getString())) this.setValue(false);
        }
    }

    @Override
    public CyclingButtonWidget<?> asButton() {
        return CyclingButtonWidget.builder(ConfigOption::getValueAsText)
                .tooltip(SimpleOption.constantTooltip(this.tooltip))
                .omitKeyText()
                .values(List.of(true, false)).initially(this.getValue())
                .build(Text.empty(), (button, value) -> this.cycle());
    }
}
