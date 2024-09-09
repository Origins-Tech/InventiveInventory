package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.screens.tabs.ConfigOptionsTab;
import net.origins.inventive_inventory.config.screens.tabs.ConfigVisualsTab;


public class ConfigScreen extends GameOptionsScreen {
    private final static String TITLE_TRANSLATION_KEY = "title." + InventiveInventory.MOD_ID + ".config_screen";
    private final static String TAB_TRANSLATION_KEY = "tab." + InventiveInventory.MOD_ID + ".config_screen.";

    public ConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.translatable(TITLE_TRANSLATION_KEY));
    }

    protected void initHeader() {
        this.layout.addHeader(new TextWidget(this.title, this.textRenderer), positioner -> positioner.relative(0.5f, 0.25f));
        this.layout.setHeaderHeight(50);
    }

    @Override
    public void addOptions() {
        ButtonWidget optionsTab = ButtonWidget.builder(Text.translatable(TAB_TRANSLATION_KEY + "options"), button -> {
            this.body = this.layout.addBody(new ConfigOptionsTab(this.client, this.width, this));
            this.layout.forEachChild(this::addDrawableChild);
        }).build();
        optionsTab.setPosition(this.width / 2 - optionsTab.getWidth() - 5, this.layout.getHeaderHeight() - optionsTab.getHeight() - 4);
        ButtonWidget visualsTab = ButtonWidget.builder(Text.translatable(TAB_TRANSLATION_KEY + "visuals"), button -> {
            this.body = this.layout.addBody(new ConfigVisualsTab(this.client, this.width, this));
            this.layout.forEachChild(this::addDrawableChild);
        }).build();
        visualsTab.setPosition(this.width / 2 + 5, this.layout.getHeaderHeight() - visualsTab.getHeight() - 4);

        this.addDrawableChild(optionsTab);
        this.addDrawableChild(visualsTab);
    }

}
