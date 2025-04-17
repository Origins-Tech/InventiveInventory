package net.inventive_mods.inventive_inventory.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.inventive_mods.inventive_inventory.features.profiles.SavedSlot;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static JsonObject itemStackToJson(ItemStack stack) {
        JsonObject stackJson = new JsonObject();
        if (stack == null) return stackJson;
        stackJson.addProperty("id", Item.getRawId(stack.getItem()));

        JsonObject componentsJson = new JsonObject();
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return stackJson;
        if (nbt.contains("custom_name")) {
            componentsJson.addProperty("custom_name", nbt.getString("custom_name"));
        }

        if (nbt.contains("Enchantments")) {
            JsonArray enchantments = new JsonArray();
            NbtList enchantmentsNbtList = nbt.getList("Enchantments", 10);
            for (NbtElement enchantmentElement : enchantmentsNbtList) {
                NbtCompound enchantmentCompound = (NbtCompound) enchantmentElement;
                JsonObject enchantment = new JsonObject();
                enchantment.addProperty("id", enchantmentCompound.getString("id"));
                enchantment.addProperty("lvl", enchantmentCompound.getShort("lvl"));
                enchantments.add(enchantment);
            }
            componentsJson.add("Enchantments", enchantments);
        }

        if (nbt.contains("Potion")) {
            componentsJson.addProperty("potion", nbt.getString("Potion"));
        }

        stackJson.add("components", componentsJson);
        return stackJson;
    }

    public static ItemStack jsonToItemStack(JsonObject stackJson) {
        if (stackJson.get("id") == null) return null;
        ItemStack item = new ItemStack(RegistryEntry.of(Item.byRawId(stackJson.get("id").getAsInt())));

        NbtCompound nbt = new NbtCompound();
        JsonObject components = stackJson.getAsJsonObject("components");
        if (components == null) return item;

        if (components.has("custom_name")) {
            nbt.putString("custom_name", components.get("custom_name").getAsString());
        }

        if (components.has("Enchantments")) {
            JsonArray enchantmentsJson = components.getAsJsonArray("Enchantments");
            NbtList enchantments = new NbtList();
            for (JsonElement enchantmentElement : enchantmentsJson) {
                JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                NbtCompound enchantment = new NbtCompound();
                enchantment.putString("id", enchantmentObject.get("id").getAsString());
                enchantment.putInt("lvl", enchantmentObject.get("lvl").getAsInt());
                enchantments.add(enchantment);
            }
            nbt.put("Enchantments", enchantments);
        }

        if (components.has("potion")) {
            nbt.putString("Potion", components.get("potion").getAsString());
        }

        item.setNbt(nbt);
        return item;
    }

    public static List<SavedSlot> jsonToSavedSlots(JsonArray savedSlotsJson) {
        List<SavedSlot> savedSlotList = new ArrayList<>();
        for (JsonElement savedSlotElement : savedSlotsJson) {
            JsonObject savedSlotObject = savedSlotElement.getAsJsonObject();
            int slot = savedSlotObject.get("slot").getAsInt();
            ItemStack stack = Converter.jsonToItemStack(savedSlotObject.getAsJsonObject("stack"));
            savedSlotList.add(new SavedSlot(slot, stack));
        }
        return savedSlotList;
    }
}
