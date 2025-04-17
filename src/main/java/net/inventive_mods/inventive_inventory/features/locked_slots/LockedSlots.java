package net.inventive_mods.inventive_inventory.features.locked_slots;

import net.inventive_mods.inventive_inventory.util.slots.PlayerSlots;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LockedSlots extends ArrayList<Integer> {
    public LockedSlots(List<Integer> list) {
        super(list);
    }

    public LockedSlots adjust() {
        int start = PlayerSlots.get().getFirst();
        return new LockedSlots(this.stream().map(slot -> slot + start).collect(Collectors.toList()));
    }

    public LockedSlots unadjust() {
        int start = PlayerSlots.get().getFirst();
        return new LockedSlots(this.stream().map(slot -> slot - start).toList());
    }
}

