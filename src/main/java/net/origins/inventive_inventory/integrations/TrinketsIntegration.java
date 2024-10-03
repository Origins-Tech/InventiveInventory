package net.origins.inventive_inventory.integrations;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.origins.inventive_inventory.InventiveInventory;

public class TrinketsIntegration {
    private static int counter;

    public static void init() {
        counter = 0;
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            TrinketsApi.getPlayerSlots(InventiveInventory.getPlayer()).forEach((s, slotGroup) -> slotGroup.getSlots().forEach((s1, slotType) -> counter += slotType.getAmount()));
        }
    }

    public static int getSlotCount() {
        return counter;
    }
}
