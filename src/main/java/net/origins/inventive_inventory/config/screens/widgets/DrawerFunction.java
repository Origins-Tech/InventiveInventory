package net.origins.inventive_inventory.config.screens.widgets;

import net.minecraft.client.gui.DrawContext;

@FunctionalInterface
public interface DrawerFunction {
    void draw(DrawContext context, int x, int y);
}
