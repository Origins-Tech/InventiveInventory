package net.origins.inventive_inventory.util.widgets;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.origins.inventive_inventory.config.screens.ConfigScreen;
import net.origins.inventive_inventory.util.widgets.screen_tab.CustomElementListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CustomListWidget extends CustomElementListWidget<CustomListWidget.WidgetEntry> {

    public CustomListWidget(MinecraftClient client, int width, ConfigScreen screen) {
        super(client, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;

    }

    @Override
    protected int getScrollbarPositionX() {
        return this.getWidth() / 2 + this.getRowWidth() / 2 + 5; // + 5 for spacing
    }

    public void addAll(List<ClickableWidget> widgets) {
        for (int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(firstWidget, secondWidget));
    }

    @Override
    public int getRowWidth() {
        if (this.children().isEmpty()) return this.width;
        AtomicInteger longest = new AtomicInteger();
        this.children().forEach(entry -> longest.set(Math.max(longest.get(), entry.widgets.get(0).getWidth())));
        return longest.get();
    }

    @Override
    public int getRowLeft() {
        return (this.width - this.getRowWidth()) / 2;
    }

    @Override
    public Optional<Element> hoveredElement(double mouseX, double mouseY) {
        return super.hoveredElement(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public @Nullable GuiNavigationPath getFocusedPath() {
        return super.getFocusedPath();
    }

    @Override
    public void focusOn(@Nullable Element element) {
        super.focusOn(element);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }

    @Environment(EnvType.CLIENT)
    protected static class WidgetEntry extends CustomElementListWidget.CustomEntry<WidgetEntry> {
        private final List<ClickableWidget> widgets;

        WidgetEntry(List<ClickableWidget> widgets) {
            this.widgets = ImmutableList.copyOf(widgets);
        }

        public static WidgetEntry create(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
            return secondWidget == null
                    ? new WidgetEntry(ImmutableList.of(firstWidget))
                    : new WidgetEntry(ImmutableList.of(firstWidget, secondWidget));
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int i = 0;
            for (ClickableWidget clickableWidget : this.widgets) {
                clickableWidget.setPosition(x + i, y);
                clickableWidget.render(context, mouseX, mouseY, tickDelta);
                i += 160;
            }
        }

        @Override
        public List<? extends Element> children() {
            return this.widgets;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.widgets;
        }
    }
}
