package storage_engine.page;

import java.util.ArrayList;
import java.util.List;

public class SlotDirectory {

    private final List<Integer> slotOffsets = new ArrayList<>();

    public int allocateSlot() {
        slotOffsets.add(0);
        return slotOffsets.size() - 1;
    }

    public void freeSlot(int slotNo) {
        if (slotNo >= 0 && slotNo < slotOffsets.size()) {
            slotOffsets.set(slotNo, -1);
        }
    }

    public List<Integer> getSlotOffsets() {
        return slotOffsets;
    }
}
