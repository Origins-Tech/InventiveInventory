package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.screens.tabs.ConfigOptionsTab;
import net.origins.inventive_inventory.config.screens.tabs.ConfigVisualsTab;

import java.util.ArrayList;
import java.util.List;


public class ConfigScreen extends GameOptionsScreen {
    private final DirectionalLayoutWidget tabButtonsLayout = DirectionalLayoutWidget.horizontal();
    private final List<ButtonWidget> tabButtons = new ArrayList<>();

    public ConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.translatable("config.screen.title.inventive_inventory.main"));
    }

    protected void initHeader() {
        this.layout.addHeader(new TextWidget(this.title, this.textRenderer), positioner -> positioner.relative(0.5f, 0.25f));
        this.layout.setHeaderHeight(50);
    }

    protected void initBody() {
        this.addOptions();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.tabButtonsLayout.setY(this.layout.getHeaderHeight() - tabButtons.getFirst().getHeight() - 4);
        this.tabButtonsLayout.setX(this.width / 2 - this.tabButtons.getFirst().getWidth());
    }

    @Override
    public void addOptions() {
        ConfigOptionsTab configOptionsTab = new ConfigOptionsTab(this.client, this.width, this);
        this.addTab("options", configOptionsTab);
        this.addTab("visuals", new ConfigVisualsTab(this.client, this.width, this));

        this.body = this.layout.addBody(configOptionsTab);
        this.tabButtons.getFirst().active = false;

        this.tabButtonsLayout.spacing(5);
        this.tabButtonsLayout.setY(this.layout.getHeaderHeight() - tabButtons.getFirst().getHeight() - 4);
        this.tabButtonsLayout.setX(this.width / 2 - this.tabButtons.getFirst().getWidth());
        this.tabButtonsLayout.refreshPositions();
        this.tabButtonsLayout.forEachChild(this::addDrawableChild);
    }

    private void addTab(String translationKey, OptionListWidget tab) {
        ButtonWidget tabButton = ButtonWidget.builder(Text.translatable("config.screen.tab.inventive_inventory." + translationKey), button -> {
            action(tab, button);
        }).build();
        this.tabButtons.add(tabButton);
        this.tabButtonsLayout.add(tabButton);
    }

    private void action(OptionListWidget tab, ButtonWidget button) {
        if (this.body != null) this.body.forEachChild(this::remove);
        this.layout.body = new SimplePositioningWidget();
        this.body = this.layout.addBody(tab);
        this.layout.forEachChild(this::addDrawableChild);
        this.tabButtons.forEach(buttonWidget -> buttonWidget.active = true);
        button.active = false;
        this.resize(this.client, this.width, this.height);
    }
}
