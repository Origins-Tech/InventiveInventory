package net.origins.inventive_inventory.config.screens.tabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.screens.ConfigScreen;
import net.origins.inventive_inventory.config.screens.widgets.ColorPickerWidget;
import net.origins.inventive_inventory.config.screens.widgets.ConfigLockedSlotWidget;
import net.origins.inventive_inventory.util.widgets.ScreenTab;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigVisualsTab extends ScreenTab {
    private final static String TITLE_TRANSLATION_KEY = "config.visuals.title." + InventiveInventory.MOD_ID;

    public ConfigVisualsTab(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addWidget(ConfigManager.SHOW_LOCK);
        this.addWidget(ConfigManager.LOCKED_SLOT_STYLE);
        this.addWidget(ConfigManager.LOCKED_SLOTS_COLOR);
        this.addEmptyRow();
        this.addCenteredWidget(new ConfigLockedSlotWidget(this.getRowWidth(), 20));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        AtomicBoolean bl = new AtomicBoolean(super.mouseClicked(mouseX, mouseY, button));
        for (WidgetEntry entry : this.children()) {
            entry.children().forEach(element -> {
                if (element instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(mouseX, mouseY) && button == 0) {
                        colorPickerWidget.clickSliderWidget(mouseX, mouseY);
                        this.setDragging(true);
                        bl.set(true);
                    }
                }
            });
        }
        return bl.get();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (WidgetEntry entry : this.children()) {
            entry.children().forEach(element -> {
                if (element instanceof ColorPickerWidget colorPickerWidget) {
                    if (colorPickerWidget.overSliderWidget(mouseX, mouseY) && button == 0) colorPickerWidget.dragSliderWidget(mouseX, mouseY, deltaX, deltaY);
                }
            });
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
