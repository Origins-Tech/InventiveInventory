package net.inventive_mods.inventive_inventory.util.widgets;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.gui.widget.Widget;

public class TabbedThreePartsLayoutWidget extends ThreePartsLayoutWidget {
    private final DirectionalLayoutWidget tabBar = DirectionalLayoutWidget.horizontal().spacing(1);
    private final Screen screen;

    public TabbedThreePartsLayoutWidget(TabbedScreen screen, int headerHeight, int footerHeight) {
        super(screen, headerHeight, footerHeight);
        this.addHeader(this.tabBar, positioner -> positioner.relativeY(0.9f));
        this.screen = screen;
    }

    public <T extends ButtonWidget> void addTabButton(T button) {
        this.tabBar.add(button);
    }

    public <T extends Widget> T replaceBody(T widget) {
        this.body.elements.clear();
        return this.body.add(widget);
    }

    public void toggleButtons(ButtonWidget pressedButton) {
        this.tabBar.forEachElement(widget -> widget.forEachChild(button -> button.active = !button.equals(pressedButton)));
    }

    public int getContentHeight() {
        return this.screen.height - this.getHeaderHeight() - this.getFooterHeight();
    }
}
