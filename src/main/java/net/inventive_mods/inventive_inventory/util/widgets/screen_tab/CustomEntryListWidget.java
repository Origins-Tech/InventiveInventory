package net.inventive_mods.inventive_inventory.util.widgets.screen_tab;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.util.widgets.WidgetHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class CustomEntryListWidget<E extends CustomEntryListWidget.CustomEntry<E>> extends CustomContainerWidget {
    protected final MinecraftClient client;
    protected final int itemHeight;
    private final Entries children = new Entries();
    private double scrollAmount;
    protected int headerHeight;
    private boolean scrolling;
    @Nullable
    private E selected;
    @Nullable
    private E hoveredEntry;

    public CustomEntryListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(0, y, width, height, ScreenTexts.EMPTY);
        this.client = client;
        this.itemHeight = itemHeight;
    }

    public int getRowWidth() {
        return 220;
    }

    /**
     * {@return the selected entry of this entry list, or {@code null} if there is none}
     */
    @Nullable
    public E getSelectedOrNull() {
        return this.selected;
    }

    public void setSelected(@Nullable E entry) {
        this.selected = entry;
    }

    @Nullable
    public E getFocused() {
        return (E)super.getFocused();
    }

    @Override
    public final List<E> children() {
        return this.children;
    }

    protected E getEntry(int index) {
        return this.children().get(index);
    }

    protected void addEntry(E entry) {
        this.children.add(entry);
    }

    protected int getEntryCount() {
        return this.children().size();
    }

    protected boolean isSelectedEntry(int index) {
        return Objects.equals(this.getSelectedOrNull(), this.children().get(index));
    }

    @Nullable
    protected final E getEntryAtPosition(double x, double y) {
        int i = this.getRowWidth() / 2;
        int j = this.getX() + this.width / 2;
        int k = j - i;
        int l = j + i;
        int m = MathHelper.floor(y - (double)this.getY()) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int n = m / this.itemHeight;
        return x < (double)this.getScrollbarPositionX() && x >= (double)k && x <= (double)l && n >= 0 && m >= 0 && n < this.getEntryCount()
                ? this.children().get(n)
                : null;
    }

    protected int getMaxPosition() {
        return this.getEntryCount() * this.itemHeight + this.headerHeight;
    }


    protected void renderHeader(DrawContext context) {
        context.setShaderColor(0.3F, 0.3F, 0.3F, 1.0F);
        context.drawTexture(
                Screen.OPTIONS_BACKGROUND_TEXTURE,
                0,
                this.width,
                0,
                this.getY(),
                -1,
                this.width,
                this.getY(),
                (float) WidgetHelper.getRight(this),
                (float) WidgetHelper.getBottom(this),
                32,
                32
        );
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderDecorations(DrawContext context) {
        context.setShaderColor(0.3F, 0.3F, 0.3F, 1.0F);
        context.drawTexture(
                Screen.OPTIONS_BACKGROUND_TEXTURE,
                0,
                this.width,
                WidgetHelper.getBottom(this),
                InventiveInventory.getScreen().height,
                -1,
                this.width,
                InventiveInventory.getScreen().height - WidgetHelper.getBottom(this),
                (float) WidgetHelper.getRight(this),
                (float) WidgetHelper.getBottom(this),
                32,
                32
        );
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hoveredEntry = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
        context.setShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
        context.drawTexture(
                Screen.OPTIONS_BACKGROUND_TEXTURE,
                this.getX(),
                this.getY(),
                (float)WidgetHelper.getRight(this),
                (float)(WidgetHelper.getBottom(this) + (int)this.getScrollAmount()),
                this.width,
                this.height,
                32,
                32
        );
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.renderHeader(context);

        this.enableScissor(context);
        this.renderList(context, mouseX, mouseY, delta);
        context.disableScissor();
        context.fillGradient(RenderLayer.getGuiOverlay(), this.getX(), this.getY(), WidgetHelper.getRight(this), this.getY() + 4, Colors.BLACK, 0, 0);
        context.fillGradient(RenderLayer.getGuiOverlay(), this.getX(), WidgetHelper.getBottom(this) - 4, WidgetHelper.getRight(this), WidgetHelper.getBottom(this), 0, Colors.BLACK, 0);

        int m = this.getMaxScroll();
        if (m > 0) {
            int n = (int)((float)((WidgetHelper.getBottom(this) - this.getY()) * (WidgetHelper.getBottom(this) - this.getY())) / (float)this.getMaxPosition());
            n = MathHelper.clamp(n, 32, WidgetHelper.getBottom(this) - this.getY() - 8);
            int o = (int)this.getScrollAmount() * (WidgetHelper.getBottom(this) - this.getY() - n) / m + this.getY();
            if (o < this.getY()) {
                o = this.getY();
            }

            int i = this.getScrollbarPositionX();
            int j = i + 6;
            context.fill(i, this.getY(), j, WidgetHelper.getBottom(this), -16777216);
            context.fill(i, o, j, o + n, -8355712);
            context.fill(i, o, j - 1, o + n - 1, -4144960);
        }

        this.renderDecorations(context);
        RenderSystem.disableBlend();
    }

    protected void enableScissor(DrawContext context) {
        context.enableScissor(this.getX(), this.getY(), WidgetHelper.getRight(this), WidgetHelper.getBottom(this));
    }

    protected void ensureVisible(E entry) {
        int i = this.getRowTop(this.children().indexOf(entry));
        int j = i - this.getY() - 4 - this.itemHeight;
        if (j < 0) {
            this.scroll(j);
        }

        int k = WidgetHelper.getBottom(this) - i - this.itemHeight - this.itemHeight;
        if (k < 0) {
            this.scroll(-k);
        }
    }

    private void scroll(int amount) {
        this.setScrollAmount(this.getScrollAmount() + (double)amount);
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double amount) {
        this.scrollAmount = MathHelper.clamp(amount, 0.0, this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.height - 4));
    }

    protected void updateScrollingState(double mouseX, int button) {
        this.scrolling = button == 0 && mouseX >= (double)this.getScrollbarPositionX() && mouseX < (double)(this.getScrollbarPositionX() + 6);
    }

    protected int getScrollbarPositionX() {
        return this.width / 2 + 124;
    }

    protected boolean isSelectButton(int button) {
        return button == 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isSelectButton(button)) {
            return false;
        } else {
            this.updateScrollingState(mouseX, button);
            if (!this.isMouseOver(mouseX, mouseY)) {
                return false;
            } else {
                E entry = this.getEntryAtPosition(mouseX, mouseY);
                if (entry != null) {
                    if (entry.mouseClicked(mouseX, mouseY, button)) {
                        E entry2 = this.getFocused();
                        if (entry2 != entry && entry2 instanceof ParentElement parentElement) {
                            parentElement.setFocused(null);
                        }

                        this.setFocused(entry);
                        this.setDragging(true);
                        return true;
                    }
                }
                return this.scrolling;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.getFocused() != null) {
            this.getFocused().mouseReleased(mouseX, mouseY, button);
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        } else if (button == 0 && this.scrolling) {
            if (mouseY < (double)this.getY()) {
                this.setScrollAmount(0.0);
            } else if (mouseY > (double)WidgetHelper.getBottom(this)) {
                this.setScrollAmount(this.getMaxScroll());
            } else {
                double d = Math.max(1, this.getMaxScroll());
                int i = WidgetHelper.getBottom(this) - this.getY();
                int j = MathHelper.clamp((int)((float)(i * i) / (float)this.getMaxPosition()), 32, i - 8);
                double e = Math.max(1.0, d / (double)(i - j));
                this.setScrollAmount(this.getScrollAmount() + deltaY * e);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.setScrollAmount(this.getScrollAmount() - amount * (double)this.itemHeight / 2.0);
        return true;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        super.setFocused(focused);
        int i = this.children.indexOf(focused);
        if (i >= 0) {
            E entry = this.children.get(i);
            this.setSelected(entry);
            if (this.client.getNavigationType().isKeyboard()) {
                this.ensureVisible(entry);
            }
        }
    }

    @Nullable
    protected E getNeighboringEntry(NavigationDirection direction, Predicate<E> predicate, @Nullable E selected) {
        int i = switch (direction) {
            case RIGHT, LEFT -> 0;
            case UP -> -1;
            case DOWN -> 1;
        };
        if (!this.children().isEmpty() && i != 0) {
            int j;
            if (selected == null) {
                j = i > 0 ? 0 : this.children().size() - 1;
            } else {
                j = this.children().indexOf(selected) + i;
            }

            for (int k = j; k >= 0 && k < this.children.size(); k += i) {
                E entry = this.children().get(k);
                if (predicate.test(entry)) {
                    return entry;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseY >= (double)this.getY() && mouseY <= (double)WidgetHelper.getBottom(this) && mouseX >= (double)this.getX() && mouseX <= (double)WidgetHelper.getRight(this);
    }

    protected void renderList(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = this.getRowLeft();
        int j = this.getRowWidth();
        int k = this.itemHeight - 4;
        int l = this.getEntryCount();

        for (int m = 0; m < l; m++) {
            int n = this.getRowTop(m);
            int o = this.getRowBottom(m);
            if (o >= this.getY() && n <= WidgetHelper.getBottom(this)) {
                this.renderEntry(context, mouseX, mouseY, delta, m, i, n, j, k);
            }
        }
    }

    protected void renderEntry(DrawContext context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
        E entry = this.getEntry(index);
        if (this.isSelectedEntry(index)) {
            int i = this.isFocused() ? -1 : -8355712;
            this.drawSelectionHighlight(context, y, entryWidth, entryHeight, i);
        }

        entry.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry), delta);
    }

    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor) {
        int i = this.getX() + (this.width - entryWidth) / 2;
        int j = this.getX() + (this.width + entryWidth) / 2;
        context.fill(i, y - 2, j, y + entryHeight + 2, borderColor);
        context.fill(i + 1, y - 1, j - 1, y + entryHeight + 1, -16777216);
    }

    public int getRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2 + 2;
    }

    protected int getRowTop(int index) {
        return this.getY() + 4 - (int)this.getScrollAmount() + index * this.itemHeight + this.headerHeight;
    }

    protected int getRowBottom(int index) {
        return this.getRowTop(index) + this.itemHeight;
    }

    @Override
    public Selectable.SelectionType getType() {
        if (this.isFocused()) {
            return Selectable.SelectionType.FOCUSED;
        } else {
            return this.hoveredEntry != null ? Selectable.SelectionType.HOVERED : Selectable.SelectionType.NONE;
        }
    }

    @Nullable
    protected E getHoveredEntry() {
        return this.hoveredEntry;
    }

    void setEntryParentList(CustomEntry<E> entry) {
        entry.parentList = this;
    }

    protected void appendNarrations(NarrationMessageBuilder builder, E entry) {
        List<E> list = this.children();
        if (list.size() > 1) {
            int i = list.indexOf(entry);
            if (i != -1) {
                builder.put(NarrationPart.POSITION, Text.translatable("narrator.position.list", i + 1, list.size()));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    class Entries extends AbstractList<E> {
        private final List<E> entries = Lists.newArrayList();

        public E get(int i) {
            return this.entries.get(i);
        }

        public int size() {
            return this.entries.size();
        }

        public E set(int i, E entry) {
            E entry2 = this.entries.set(i, entry);
            CustomEntryListWidget.this.setEntryParentList(entry);
            return entry2;
        }

        public void add(int i, E entry) {
            this.entries.add(i, entry);
            CustomEntryListWidget.this.setEntryParentList(entry);
        }

        public E remove(int i) {
            return this.entries.remove(i);
        }
    }

    @Environment(EnvType.CLIENT)
    protected abstract static class CustomEntry<E extends CustomEntry<E>> implements Element {
        @Deprecated
        CustomEntryListWidget<E> parentList;

        @Override
        public void setFocused(boolean focused) {
        }

        @Override
        public boolean isFocused() {
            return this.parentList.getFocused() == this;
        }

        /**
         * Renders an entry in a list.
         *
         * @param x the X coordinate of the entry
         * @param y the Y coordinate of the entry
         * @param index the index of the entry
         * @param hovered whether the mouse is hovering over the entry
         * @param mouseY the Y coordinate of the mouse
         * @param mouseX the X coordinate of the mouse
         * @param entryHeight the height of the entry
         * @param entryWidth the width of the entry
         */
        public abstract void render(
                DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta
        );

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return Objects.equals(this.parentList.getEntryAtPosition(mouseX, mouseY), this);
        }
    }
}