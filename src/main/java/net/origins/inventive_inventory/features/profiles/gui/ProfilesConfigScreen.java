package net.origins.inventive_inventory.features.profiles.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.features.profiles.gui.widgets.ProfileEntry;

import java.util.ArrayList;
import java.util.List;

public class ProfilesConfigScreen extends GameOptionsScreen {
    private final static String TITLE_TRANSLATION_KEY = "title." + InventiveInventory.MOD_ID + ".profiles_config_screen";
    private final static String TEXT_TRANSLATION_KEY = "text." + InventiveInventory.MOD_ID + ".profiles_config_screen.";
    private final static String DEFAULT_TRANSLATION_KEY = "default." + InventiveInventory.MOD_ID + ".";
    public final List<Text> availableKeys = new ArrayList<>();
    private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical();

    public ProfilesConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.translatable(TITLE_TRANSLATION_KEY));
        MinecraftClient client = InventiveInventory.getClient();
        this.width = client.getWindow().getScaledWidth();
        this.height = client.getWindow().getScaledHeight();

        List<KeyBinding> availableBindings = ProfileHandler.getAvailableProfileKeys();
        availableBindings.forEach(binding -> {
            if (binding != null) availableKeys.add(binding.getBoundKeyLocalizedText());
        });
        this.availableKeys.add(Text.translatable(DEFAULT_TRANSLATION_KEY + "not_bound"));

        this.init(client, this.width, this.height);
        this.layout.forEachElement(element -> ((ProfileEntry) element).forEachElement(profileElement -> {
            if (profileElement instanceof ClickableWidget) this.addDrawableChild(((ClickableWidget) profileElement));
            else if (profileElement instanceof Drawable) this.addDrawable(((Drawable) profileElement));
        }));
    }

    @Override
    protected void addOptions() {
        if (this.body == null || this.client == null) return;
        int x = this.body.getX() + 5;
        int y = this.body.getY() + 5;

        int i = 1;
        for (Profile profile : ProfileHandler.getProfiles()) {
            y += 30;
            this.layout.add(new ProfileEntry(i, x, y, profile, this));
            i++;
        }
        if (i == 1) {
            TextWidget textWidget = new TextWidget(Text.translatable(TEXT_TRANSLATION_KEY + "no_profiles"), this.client.textRenderer);
            textWidget.setPosition(this.width / 2 - textWidget.getWidth() / 2, this.height / 2 - textWidget.getHeight() / 2);
            this.addDrawableChild(textWidget);
        } else this.initBodyHeader();
    }

    private void initBodyHeader() {
        if (client == null) return;
        if (this.body == null) return;
        DirectionalLayoutWidget labelLayout = DirectionalLayoutWidget.horizontal();
        TextWidget name = new TextWidget(80, 20, Text.translatable(TEXT_TRANSLATION_KEY + "name"), this.client.textRenderer).alignCenter();
        TextWidget key = new TextWidget(60, 20, Text.translatable(DEFAULT_TRANSLATION_KEY + "key"), this.client.textRenderer).alignCenter();
        TextWidget preview = new TextWidget(205, 20, Text.translatable(TEXT_TRANSLATION_KEY + "preview"), this.client.textRenderer).alignCenter();
        labelLayout.add(name);
        labelLayout.add(key);
        labelLayout.add(EmptyWidget.ofWidth(30));
        labelLayout.add(preview);
        labelLayout.spacing(10);
        labelLayout.refreshPositions();
        labelLayout.setPosition(50 + 5, this.body.getY() + 10);
        labelLayout.forEachElement(element -> {
            if (element instanceof TextWidget) this.addDrawable((Drawable) element);
        });
    }

    @Override
    protected void initFooter() {
        super.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.layout.forEachElement(element -> ((ProfileEntry) element).updateProfile());
            this.close();
        }).width(200).build());
    }
}
