package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.options.ConfigOption;
import net.origins.inventive_inventory.config.screens.widgets.ConfigTextWidget;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesConfigScreen;


public class ConfigScreen extends GameOptionsScreen {
    private final static String TITLE_TRANSLATION_KEY = "title." + InventiveInventory.MOD_ID + ".config_screen";
    private final static String TEXT_TRANSLATION_KEY = "text." + InventiveInventory.MOD_ID + ".config_screen.";

    public ConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.translatable(TITLE_TRANSLATION_KEY));
    }

    @Override
    public void addOptions() {
        if (client == null || body == null) return;
        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".sorting"));
        this.addWidget(ConfigManager.SORTING);
        this.addWidget(ConfigManager.SORTING_MODE);
        this.addWidget(ConfigManager.CURSOR_STACK_BEHAVIOUR);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".automatic_refilling"));
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_MODE);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_BEHAVIOUR);
        this.addWidget(ConfigManager.TOOL_REPLACEMENT_PRIORITY);
        this.addWidget(ConfigManager.AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".profiles"));
        this.addWidget(ConfigManager.PROFILES);
        this.body.addWidgetEntry(new ConfigTextWidget(Text.translatable(TEXT_TRANSLATION_KEY + "config_screen"), this.client.textRenderer), ButtonWidget.builder(Text.translatable(TEXT_TRANSLATION_KEY + "config_profiles"), button -> this.client.setScreen(new ProfilesConfigScreen(this))).build());
        this.addWidget(ConfigManager.FAST_LOAD);
        this.addWidget(ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addWidget(ConfigManager.PICKUP_INTO_LOCKED_SLOTS);
    }

    private void addTitle(Text title) {
        if (this.body == null || client == null) return;
        TextWidget text = new ConfigTextWidget(title.copy().setStyle(Style.EMPTY.withBold(true)), client.textRenderer);
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
