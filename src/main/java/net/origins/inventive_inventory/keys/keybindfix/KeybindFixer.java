package net.origins.inventive_inventory.keys.keybindfix;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.keybindfix.mixins.MixinIKeyBindingAccessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeybindFixer {

    private static final Multimap<InputUtil.Key, KeyBinding> keyFixMap = ArrayListMultimap.create();

    public static void putKey(InputUtil.Key key, KeyBinding keyBinding) {
        keyFixMap.put(key, keyBinding);
    }

    public static void clearMap() {
        keyFixMap.clear();
    }

    public static boolean checkProfileKeys(InputUtil.Key key) {
        List<KeyBinding> profileKeys = new ArrayList<>(Arrays.asList(KeyRegistry.profileKeys));
        profileKeys.removeAll(ProfileHandler.getAvailableProfileKeys());
        List<KeyBinding> keyBindings = keyFixMap.get(key).stream().toList();
        for (KeyBinding keyBind : keyBindings) {
            if (profileKeys.contains(keyBind)) {
                if (KeyRegistry.loadProfileKey.isPressed() || ConfigManager.FAST_LOAD.is(true)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void onKeyPressed(InputUtil.Key key, KeyBinding finalBinding, KeyBinding baseBinding) {
        if (finalBinding != baseBinding) return;
        for (KeyBinding keyBinding : keyFixMap.get(key)) {
            if (keyBinding == null || keyBinding == baseBinding) continue;
            MixinIKeyBindingAccessor keyBindingAccessor = (MixinIKeyBindingAccessor) keyBinding;
            keyBindingAccessor.setTimesPressed(keyBindingAccessor.getTimesPressed() + 1);
        }
    }

    public static void setKeyPressed(InputUtil.Key key, boolean pressed, KeyBinding finalBinding, KeyBinding baseBinding) {
        if (finalBinding != baseBinding) return;
        for (KeyBinding keyBinding : keyFixMap.get(key)) {
            if (keyBinding == null || keyBinding == baseBinding) continue;
            keyBinding.setPressed(pressed);
        }
    }
}
