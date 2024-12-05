package net.origins.inventive_inventory.keys.keybindfix.mixins;


import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface MixinIKeyBindingAccessor {

    @Accessor(value = "timesPressed")
    int getTimesPressed();

    @Accessor(value = "timesPressed")
    void setTimesPressed(int value);
}
