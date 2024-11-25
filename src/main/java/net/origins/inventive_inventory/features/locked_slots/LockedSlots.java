package net.origins.inventive_inventory.features.locked_slots;

import net.origins.inventive_inventory.util.slots.PlayerSlots;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LockedSlots extends ArrayList<Integer> {
    public LockedSlots(List<Integer> list) {
        super(list);
    }

    public LockedSlots adjust() {
        int start = PlayerSlots.get().get(0);
        return new LockedSlots(this.stream().map(slot -> slot + start).collect(Collectors.toList()));
    }

    public LockedSlots unadjust() {
        int start = PlayerSlots.get().get(0);
        return new LockedSlots(this.stream().map(slot -> slot - start).toList());
    }
}

