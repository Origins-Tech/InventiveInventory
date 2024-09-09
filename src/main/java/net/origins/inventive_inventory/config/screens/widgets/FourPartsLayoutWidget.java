package net.origins.inventive_inventory.config.screens.widgets;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class FourPartsLayoutWidget implements LayoutWidget {
    public static final int DEFAULT_HEADER_FOOTER_HEIGHT = 33;
    private static final int FOOTER_MARGIN_TOP = 30;
    private static final int BODY_HEADER_HEIGHT = 17;
    private final SimplePositioningWidget header = new SimplePositioningWidget();
    private final SimplePositioningWidget footer = new SimplePositioningWidget();
    private final SimplePositioningWidget bodyHeader = new SimplePositioningWidget();
    private final SimplePositioningWidget body = new SimplePositioningWidget();
    private final Screen screen;
    private int headerHeight;
    private int footerHeight;
    private final int bodyHeaderHeight;

    public FourPartsLayoutWidget(Screen screen) {
        this(screen, DEFAULT_HEADER_FOOTER_HEIGHT);
    }

    public FourPartsLayoutWidget(Screen screen, int headerFooterHeight) {
        this(screen, headerFooterHeight, headerFooterHeight, BODY_HEADER_HEIGHT);
    }

    public FourPartsLayoutWidget(Screen screen, int headerFooterHeight, int bodyHeaderHeight) {
        this(screen, headerFooterHeight, headerFooterHeight, bodyHeaderHeight);
    }

    public FourPartsLayoutWidget(Screen screen, int headerHeight, int footerHeight, int bodyHeaderHeight) {
        this.screen = screen;
        this.headerHeight = headerHeight;
        this.footerHeight = footerHeight;
        this.bodyHeaderHeight = bodyHeaderHeight;
        this.bodyHeader.getMainPositioner().relative(0.5F, 0.5F);
        this.header.getMainPositioner().relative(0.5F, 0.5F);
        this.footer.getMainPositioner().relative(0.5F, 0.5F);
    }

    @Override
    public void setX(int x) {
    }

    @Override
    public void setY(int y) {
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return this.screen.width;
    }

    @Override
    public int getHeight() {
        return this.screen.height;
    }

    public int getFooterHeight() {
        return this.footerHeight;
    }

    public void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    public int getBodyHeaderHeight() {
        return this.bodyHeaderHeight;
    }

    public int getContentHeight() {
        return this.screen.height - this.getHeaderHeight() - this.getFooterHeight() - this.getBodyHeaderHeight();
    }

    public int getAllHeaderHeight() {
        return this.headerHeight + this.bodyHeaderHeight;
    }

    @Override
    public void forEachElement(Consumer<Widget> consumer) {
        this.header.forEachElement(consumer);
        this.body.forEachElement(consumer);
        this.bodyHeader.forEachElement(consumer);
        this.footer.forEachElement(consumer);
    }

    @Override
    public void refreshPositions() {
        int i = this.getHeaderHeight();
        int j = this.getFooterHeight();
        int m = this.getBodyHeaderHeight();
        this.header.setMinWidth(this.screen.width);
        this.header.setMinHeight(i);
        this.header.setPosition(0, 0);
        this.header.refreshPositions();
        this.bodyHeader.setMinWidth(this.screen.width);
        this.bodyHeader.setMinHeight(m);
        this.bodyHeader.refreshPositions();
        this.bodyHeader.setPosition(0, i);
        this.footer.setMinWidth(this.screen.width);
        this.footer.setMinHeight(j);
        this.footer.refreshPositions();
        this.footer.setY(this.screen.height - j);
        this.body.setMinWidth(this.screen.width);
        this.body.refreshPositions();
        int k = i + m + FOOTER_MARGIN_TOP;
        int l = this.screen.height - j - this.body.getHeight() - this.bodyHeader.getHeight();
        this.body.setPosition(0, Math.min(k, l));
    }

    public <T extends Widget> T addHeader(T widget) {
        return this.header.add(widget);
    }

    public <T extends Widget> T addHeader(T widget, Consumer<Positioner> callback) {
        return this.header.add(widget, callback);
    }

    public void addHeader(Text text, TextRenderer textRenderer) {
        this.header.add(new TextWidget(text, textRenderer), Positioner.create().relative(0.5F, 0.6F));
    }

    public <T extends Widget> void addFooter(T widget) {
        this.footer.add(widget);
    }

    public <T extends Widget> T addBody(T widget) {
        return this.body.add(widget);
    }

    public void addBodyHeader(ButtonWidget... buttonWidgets) {
        float relativeX = 0.25f;
        for (ButtonWidget buttonWidget : buttonWidgets) {
            this.bodyHeader.add(buttonWidget, Positioner.create().relativeX(relativeX));
            relativeX += 0.5f;
        }
    }
}
