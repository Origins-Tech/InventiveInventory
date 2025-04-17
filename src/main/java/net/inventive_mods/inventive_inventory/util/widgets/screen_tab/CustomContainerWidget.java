package net.inventive_mods.inventive_inventory.util.widgets.screen_tab;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public abstract class CustomContainerWidget extends ClickableWidget implements ParentElement {
    @Nullable
    private Element focusedElement;
    private boolean dragging;

    public CustomContainerWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Override
    public final boolean isDragging() {
        return this.dragging;
    }

    @Override
    public final void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Nullable
    @Override
    public Element getFocused() {
        return this.focusedElement;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        if (this.focusedElement != null) {
            this.focusedElement.setFocused(false);
        }

        if (focused != null) {
            focused.setFocused(true);
        }

        this.focusedElement = focused;
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return ParentElement.super.getNavigationPath(navigation);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return ParentElement.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return ParentElement.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return ParentElement.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean isFocused() {
        return ParentElement.super.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        ParentElement.super.setFocused(focused);
    }

    protected void setDimensions(int width, int contentHeight) {
        this.width = width;
        this.height = contentHeight;
    }
}
