package storage_engine.buffer;

import storage_engine.interfaces.PageReplacementStrategy;

public class FIFOReplacementStrategy implements PageReplacementStrategy {

    @Override
    public int selectVictim() {
        System.out.println("  -> [Strategy: FIFO] Chọn khung bộ đệm được nạp vào RAM đầu tiên (First In First Out).");
        return 2;
    }
}
