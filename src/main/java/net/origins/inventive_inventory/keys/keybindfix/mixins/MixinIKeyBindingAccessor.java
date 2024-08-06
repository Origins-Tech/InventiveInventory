package net.origins.inventive_inventory.keys.keybindfix.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(KeyBinding.class)
public interface MixinIKeyBindingAccessor {

    @Accessor(value = "timesPressed")
    int getTimesPressed();

    @Accessor(value = "timesPressed")
    void setTimesPressed(int value);
}
