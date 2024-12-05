package net.origins.inventive_inventory.config.screens.widgets;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.options.fields.ColorFieldOption;

public class ColorFieldWidget extends TextFieldWidget {
    private final ColorFieldOption option;

    public ColorFieldWidget(Text text, ColorFieldOption option) {
        super(InventiveInventory.getClient().textRenderer, 0, 0, 50, 20, Text.empty());
        this.option = option;
        this.setText("#" + text.getString());
        this.setMaxLength(7);
    }

    @Override
    public void onChanged(String newText) {
        super.onChanged(newText);
        if (newText.isEmpty() || newText.charAt(0) != '#') {
            this.setText("#" + newText);
            this.setCursor(1);
        } else if (this.getCursor() == 0) this.setCursor(1);
        try {
            int parsedColor = Integer.parseInt(newText.substring(1), 16);
            this.option.setValue(ColorHelper.Argb.getArgb(ColorHelper.Argb.getAlpha(this.option.getValue()), ColorHelper.Argb.getRed(parsedColor), ColorHelper.Argb.getGreen(parsedColor), ColorHelper.Argb.getBlue(parsedColor)));
        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
            this.option.setValue(ColorHelper.Argb.getArgb(ColorHelper.Argb.getAlpha(option.getValue()), ColorHelper.Argb.getRed(this.option.getDefaultValue()), ColorHelper.Argb.getGreen(this.option.getDefaultValue()), ColorHelper.Argb.getBlue(this.option.getDefaultValue())));
        }
    }

    public void reset() {
        this.setText("#" + Integer.toHexString(this.option.getDefaultValue()).substring(2));
        this.option.reset();
    }
}
