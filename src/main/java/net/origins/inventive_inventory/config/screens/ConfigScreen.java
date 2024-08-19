package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.options.ConfigOption;
import net.origins.inventive_inventory.config.screens.widgets.ConfigTextWidget;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesConfigScreen;


public class ConfigScreen extends GameOptionsScreen {
    private OptionListWidget body;

    public ConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.of("Inventive Inventory Options"));
    }

    @Override
    protected void init() {
        this.body = new OptionListWidget(this.client, parent.width, 0, this);
        this.initHeader();
        this.initFooter();
        this.addOptions();
        this.layout.addBody(body);
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    private void addOptions() {
        if (client == null || body == null) return;
        this.addTitle("Sorting");
        this.addWidget(ConfigManager.SORTING);
        this.addWidget(ConfigManager.SORTING_MODE);
        this.addWidget(ConfigManager.CURSOR_STACK_BEHAVIOUR);

        this.addTitle("Automatic Refilling");
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_MODE);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_PRIORITY);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS);

        this.addTitle("Profiles");
        this.addWidget(ConfigManager.PROFILES);
        this.body.addWidgetEntry(new ConfigTextWidget(Text.of("Config Screen:"), this.client.textRenderer), ButtonWidget.builder(Text.of("Config Profiles..."), button -> this.client.setScreen(new ProfilesConfigScreen(this))).build());
        this.addWidget(ConfigManager.FAST_LOAD);
        this.addWidget(ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS);
    }

    private void addTitle(String title) {
        if (this.body == null || client == null) return;
        TextWidget text = new ConfigTextWidget(Text.of(title).copy().setStyle(Style.EMPTY.withBold(true)), client.textRenderer);
        text.setWidth(310);
        this.body.addWidgetEntry(text, null);
    }

    private void addWidget(ConfigOption<?> option) {
        if (this.body == null || client == null) return;
        this.body.addWidgetEntry(createTextWidget(option), option.asButton());
    }

    private TextWidget createTextWidget(ConfigOption<?> option) {
        if (client == null) return null;
        return new ConfigTextWidget(Text.translatable(option.getTranslationKey()), client.textRenderer).alignCenter();
    }
}
