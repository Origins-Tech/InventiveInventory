package net.origins.inventive_inventory.config.screens.tabs;

import net.minecraft.client.MinecraftClient;
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

public class ConfigOptionsTab extends OptionListWidget {
    private final static String TITLE_TRANSLATION_KEY = "title." + InventiveInventory.MOD_ID + ".config_screen";
    private final static String TEXT_TRANSLATION_KEY = "text." + InventiveInventory.MOD_ID + ".config_screen.";

    public ConfigOptionsTab(MinecraftClient client, int width, GameOptionsScreen optionsScreen) {
        super(client, width, optionsScreen);

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
        this.addWidgetEntry(new ConfigTextWidget(Text.translatable(TEXT_TRANSLATION_KEY + "config_screen"), this.client.textRenderer), ButtonWidget.builder(Text.translatable(TEXT_TRANSLATION_KEY + "config_profiles"), button -> this.client.setScreen(new ProfilesConfigScreen(optionsScreen))).build());
        this.addWidget(ConfigManager.FAST_LOAD);
        this.addWidget(ConfigManager.PROFILES_IGNORE_LOCKED_SLOTS);

        this.addTitle(Text.translatable(TITLE_TRANSLATION_KEY + ".locked_slots"));
        this.addWidget(ConfigManager.PICKUP_INTO_LOCKED_SLOTS);
        this.addWidget(ConfigManager.QUICK_MOVE_INTO_LOCKED_SLOTS);
    }

    private void addTitle(Text title) {
        if (client == null) return;
        TextWidget text = new ConfigTextWidget(title.copy().setStyle(Style.EMPTY.withBold(true)), client.textRenderer);
        text.setWidth(310);
        this.addWidgetEntry(text, null);
    }

    private void addWidget(ConfigOption<?> option) {
        if (client == null) return;
        this.addWidgetEntry(createTextWidget(option), option.asButton());
    }

    private TextWidget createTextWidget(ConfigOption<?> option) {
        if (client == null) return null;
        return new ConfigTextWidget(Text.translatable(option.getTranslationKey()), client.textRenderer).alignCenter();
    }
}
