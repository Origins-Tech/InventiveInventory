package net.origins.inventive_inventory.features.profiles.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesConfigScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;

public class ProfileEntry extends DirectionalLayoutWidget implements Drawable {
    private final TextFieldWidget nameTextField;
    private final ButtonWidget keyButton;
    private final Profile profile;
    private final ProfilesConfigScreen parent;


    public ProfileEntry(int index, int x, int y, Profile profile, ProfilesConfigScreen parent) {
        super(x, y, DisplayAxis.HORIZONTAL);
        this.profile = profile;
        this.parent = parent;
        int height = 20;
        MinecraftClient client = InventiveInventory.getClient();
        TextWidget indexText = new TextWidget(40, height, Text.of(index + "."), client.textRenderer);
        this.nameTextField = new TextFieldWidget(client.textRenderer, 80, height, Text.empty());
        this.nameTextField.setText(profile.getName());
        this.nameTextField.setPlaceholder(Text.of("Name..."));

        KeyBinding profileKey = KeyRegistry.getByTranslationKey(this.profile.getKey());
        Text initially = profileKey != null ? profileKey.getBoundKeyLocalizedText() : Text.of("Not Bound");

        this.keyButton = ButtonWidget.builder(initially, this.toggle()).build();
        this.keyButton.setWidth(60);

        Hotbar hotbar = new Hotbar(200, y, profile.getSavedSlots());

        this.add(indexText);
        this.add(this.nameTextField);
        this.add(keyButton);
        this.add(EmptyWidget.ofWidth(30));
        this.add(hotbar);
        this.spacing(10);
        this.refreshPositions();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.forEachElement(element -> {
            if (element instanceof ClickableWidget) ((ClickableWidget) element).render(context, mouseX, mouseY, delta);
        });
    }

    public void updateProfile() {
        String name = this.nameTextField.getText();
        KeyBinding keyBinding = KeyRegistry.getByBoundKey(this.keyButton.getMessage().getString());
        String key = keyBinding != null ? keyBinding.getTranslationKey() : "";
        if (!name.equals(this.profile.getName()) || !key.equals(this.profile.getKey())) {
            this.profile.setName(name);
            this.profile.setKey(key);
            ProfileHandler.update(this.profile);
        }
    }

    private ButtonWidget.PressAction toggle() {
        return button -> {
            Text message = button.getMessage();
            Text newMessage = this.parent.availableKeys.getFirst();
            if (message.getString().equals(newMessage.getString())) {
                this.parent.availableKeys.remove(newMessage);
                this.parent.availableKeys.add(newMessage);
                newMessage = this.parent.availableKeys.getFirst();
            }
            if (!message.getString().equals("Not Bound")) {
                this.parent.availableKeys.add(message);
            }
            if (!newMessage.getString().equals("Not Bound")) {
                this.parent.availableKeys.remove(newMessage);
            }
            button.setMessage(newMessage);
            this.profile.setKey(button.getMessage().getString());
        };
    }

}