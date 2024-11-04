package net.origins.inventive_inventory.util.widgets;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class TabbedScreen extends Screen {
    private final Screen parent;
    protected static ScreenTab activeTab;
    protected final List<ScreenTab> tabs = new ArrayList<>();
    protected final TabbedThreePartsLayoutWidget layout = new TabbedThreePartsLayoutWidget(this, 50, 33);

    public TabbedScreen(Screen parent, String titleTranslationKey) {
        super(Text.translatable(titleTranslationKey));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.initHeader();
        this.addTabs();
        this.initBody();
        this.initFooter();
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    protected void initHeader() {
        this.layout.addHeader(new TextWidget(this.title, this.textRenderer), positioner -> positioner.relativeY(0.25f));
    }

    protected void addTabs() {

    }

    protected void initBody() {
        activeTab = this.layout.addBody(this.tabs.getFirst());
    }

    protected void initFooter() {
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
        if (activeTab != null) {
            activeTab.position(this.width, this.layout);
        }
    }

    @Override
    public void close() {
        this.tabs.forEach(ScreenTab::onClose);
        if (this.client != null) this.client.setScreen(this.parent);
    }

    protected void addTab(String translationKey, ScreenTab tab, boolean active) {
        ButtonWidget tabButton = ButtonWidget.builder(Text.translatable("config.screen.tab.inventive_inventory." + translationKey), button -> {
            this.remove(activeTab);
            activeTab = this.layout.replaceBody(tab);
            this.addDrawableChild(tab);
            this.layout.toggleButtons(button);
        }).build();
        tabButton.active = !active;
        this.layout.addTabButton(tabButton);
        this.tabs.add(tab);
    }
}
