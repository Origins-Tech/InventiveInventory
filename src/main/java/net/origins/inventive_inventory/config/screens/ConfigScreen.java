package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;


public class ConfigScreen extends GameOptionsScreen {
    private OptionListWidget body;
    private final static String TITLE_TRANSLATION_KEY = "title." + InventiveInventory.MOD_ID + ".config_screen";

    public ConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.translatable(TITLE_TRANSLATION_KEY));
    }

    @Override
    protected void init() {
        this.body = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addOptions();
        this.addDrawableChild(this.body);
        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close())
                        .position(this.width / 2 - 100, this.height - 27)
                        .size(200, 20)
                        .build());
    }

    @Override
    public void render(DrawContext DrawContext, int mouseX, int mouseY, float delta) {
        super.render(DrawContext, mouseX, mouseY, delta);
        DrawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
    }

    public void addOptions() {
        if (this.client == null || this.body == null) return;
        this.body.addSingleOptionEntry(ConfigManager.SORTING.asButton());
        this.body.addOptionEntry(ConfigManager.SORTING_MODE.asButton(), ConfigManager.CURSOR_STACK_BEHAVIOUR.asButton());

        this.body.addSingleOptionEntry(ConfigManager.AUTOMATIC_REFILLING.asButton());
        this.body.addOptionEntry(ConfigManager.AUTOMATIC_REFILLING_MODE.asButton(), ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS.asButton());
        this.body.addOptionEntry(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR.asButton(), ConfigManager.TOOL_REPLACEMENT_PRIORITY.asButton());

        this.body.addSingleOptionEntry(ConfigManager.PROFILES.asButton());
        this.body.addOptionEntry(ConfigManager.FAST_LOAD.asButton(), ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS.asButton());
    }
}
