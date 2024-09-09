package net.origins.inventive_inventory.config.screens.tabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.screens.widgets.ConfigTextWidget;

public class ConfigVisualsTab extends OptionListWidget {
    public ConfigVisualsTab(MinecraftClient client, int width, GameOptionsScreen optionsScreen) {
        super(client, width, optionsScreen);

        this.addTitle(Text.of("DUMM WENNS FUNKTIONIERT"));
        this.addTitle(Text.of("DUMM WENNS FUNKTIONIERT"));
        this.addTitle(Text.of("DUMM WENNS FUNKTIONIERT"));
        this.addTitle(Text.of("DUMM WENNS FUNKTIONIERT"));
        this.addTitle(Text.of("DUMM WENNS FUNKTIONIERT"));
    }

    private void addTitle(Text title) {
        if (client == null) return;
        TextWidget text = new ConfigTextWidget(title.copy().setStyle(Style.EMPTY.withBold(true)), client.textRenderer);
        text.setWidth(310);
        this.addWidgetEntry(text, null);
    }
}
