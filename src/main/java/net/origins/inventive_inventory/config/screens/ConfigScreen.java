package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.gui.screen.Screen;
import net.origins.inventive_inventory.config.screens.tabs.ConfigOptionsTab;
import net.origins.inventive_inventory.config.screens.tabs.ConfigProfilesTab;
import net.origins.inventive_inventory.config.screens.tabs.ConfigVisualsTab;
import net.origins.inventive_inventory.util.widgets.TabbedScreen;


public class ConfigScreen extends TabbedScreen {
    public ConfigScreen(Screen parent) {
        super(parent, "config.screen.title.inventive_inventory.main");
    }

    @Override
    protected void addTabs() {
        this.addTab("options", new ConfigOptionsTab(this.client, this.width, this), true);
        this.addTab("visuals", new ConfigVisualsTab(this.client, this.width, this), false);
        this.addTab("profiles", new ConfigProfilesTab(this.client, this.width, this), false);
    }
}
