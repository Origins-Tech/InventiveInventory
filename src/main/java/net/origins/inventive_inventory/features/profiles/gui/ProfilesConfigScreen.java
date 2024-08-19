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
    public final List<Text> availableKeys = new ArrayList<>();
    private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical();
    private OptionListWidget body;

    public ProfilesConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.of("Profiles Config Screen"));
        MinecraftClient client = InventiveInventory.getClient();
        this.width = client.getWindow().getScaledWidth();
        this.height = client.getWindow().getScaledHeight();
        this.body = new OptionListWidget(this.client, this.width, this.height, this);

        List<KeyBinding> availableBindings = ProfileHandler.getAvailableProfileKeys();
        availableBindings.forEach(binding -> {
            if (binding != null) availableKeys.add(binding.getBoundKeyLocalizedText());
        });
        this.availableKeys.add(Text.of("Not Bound"));

        this.init(client, this.width, this.height);
        this.layout.forEachElement(element -> ((ProfileEntry) element).forEachElement(profileElement -> {
            if (profileElement instanceof ClickableWidget) this.addDrawableChild(((ClickableWidget) profileElement));
            else if (profileElement instanceof Drawable) this.addDrawable(((Drawable) profileElement));
        }));
    }

    @Override
    protected void init() {
        super.initHeader();
        super.initFooter();
        this.body = new OptionListWidget(this.client, this.width, this.height, this);
        super.layout.addBody(body);
        this.addOptions();
        super.layout.forEachChild(this::addDrawableChild);
        super.initTabNavigation();
    }

    private void addOptions() {
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
            TextWidget textWidget = new TextWidget(Text.of("You have no profiles in this world."), this.client.textRenderer);
            textWidget.setPosition(this.width / 2 - textWidget.getWidth() / 2, this.height / 2 - textWidget.getHeight() / 2);
            this.addDrawableChild(textWidget);
        } else this.initBodyHeader();
    }

    private void initBodyHeader() {
        if (client == null) return;
        if (this.body == null) return;
        DirectionalLayoutWidget labelLayout = DirectionalLayoutWidget.horizontal();
        TextWidget name = new TextWidget(80, 20, Text.of("Profile Name"), this.client.textRenderer).alignCenter();
        TextWidget key = new TextWidget(60, 20, Text.of("Key"), this.client.textRenderer).alignCenter();
        TextWidget preview = new TextWidget(205, 20, Text.of("Preview"), this.client.textRenderer).alignCenter();
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
