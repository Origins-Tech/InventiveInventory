package net.origins.inventive_inventory.util;

import net.minecraft.client.gui.widget.ClickableWidget;

public class WidgetHelper {

    public static int getRight(ClickableWidget widget) {
        return widget.getX() + widget.getWidth();
    }

    public static int getBottom(ClickableWidget widget) {
        return widget.getY() + widget.getHeight();
    }
}
