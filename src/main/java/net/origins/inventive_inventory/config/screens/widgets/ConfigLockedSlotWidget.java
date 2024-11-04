package net.origins.inventive_inventory.config.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.origins.inventive_inventory.util.Drawer;
import net.origins.inventive_inventory.util.widgets.CustomClickableWidget;

public class ConfigLockedSlotWidget extends CustomClickableWidget {

    public ConfigLockedSlotWidget(int width, int height) {
        super(width, height);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Drawer.drawLockedSlot(context, this.getX() + this.getWidth() / 2 - 10, this.getY());
    }
}
