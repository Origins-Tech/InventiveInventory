package net.origins.inventive_inventory.features.profiles.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.WrapperWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.SavedSlot;
import net.origins.inventive_inventory.util.Drawer;

import java.util.List;
import java.util.function.Consumer;

public class Hotbar extends WrapperWidget implements Drawable {
    private final List<SavedSlot> savedSlots;

    public Hotbar(int x, int y, List<SavedSlot> savedSlots) {
        super(x, y, 205, 20);
        this.savedSlots = savedSlots;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Drawer.drawProfileHotbar(context, this.getX(), this.getY());

        int slotX = this.getX() + 27;
        int slotY = this.getY() + 2;
        for (SavedSlot savedSlot : this.savedSlots) {
            if (savedSlot.slot() == PlayerScreenHandler.OFFHAND_ID) {
                context.drawItem(savedSlot.stack(), this.getX() + 2, slotY);
                boolean inX = this.getX() + 2 < mouseX && mouseX < this.getX() + 2 + 16;
                boolean inY = slotY < mouseY && mouseY < slotY + 16;
                boolean isMouseOverItem = inX && inY;
                if (isMouseOverItem)
                    context.drawItemTooltip(InventiveInventory.getClient().textRenderer, savedSlot.stack(), mouseX, mouseY);
            }
            for (int i = 0; i < 9; i++) {
                if (savedSlot.slot() - PlayerInventory.MAIN_SIZE == i) {
                    context.drawItem(savedSlot.stack(), slotX, slotY);
                    boolean inX = slotX < mouseX && mouseX < slotX + 16;
                    boolean inY = slotY < mouseY && mouseY < slotY + 16;
                    boolean isMouseOverItem = inX && inY;
                    if (isMouseOverItem)
                        context.drawItemTooltip(InventiveInventory.getClient().textRenderer, savedSlot.stack(), mouseX, mouseY);
                    break;
                }
            }
            slotX += 20;
        }
    }

    @Override
    public void forEachElement(Consumer<Widget> consumer) {
    }
}
