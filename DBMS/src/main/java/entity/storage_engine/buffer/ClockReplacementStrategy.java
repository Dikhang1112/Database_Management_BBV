package entity.storage_engine.buffer;

import entity.storage_engine.interfaces.PageReplacementStrategy;

public class ClockReplacementStrategy implements PageReplacementStrategy {

    @Override
    public int selectVictim() {
        System.out.println("  -> [Strategy: Clock] Chọn khung bộ đệm theo thuật toán Kim đồng hồ (Second Chance Algorithm).");
        return 1;
    }
}
