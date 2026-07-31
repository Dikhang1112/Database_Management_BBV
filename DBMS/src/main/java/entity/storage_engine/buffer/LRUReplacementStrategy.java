package entity.storage_engine.buffer;

import entity.storage_engine.interfaces.PageReplacementStrategy;

public class LRUReplacementStrategy implements PageReplacementStrategy {

    @Override
    public int selectVictim() {
        System.out.println("  -> [Strategy: LRU] Chọn khung bộ đệm ít được dùng gần nhất (Least Recently Used) để giải phóng.");
        return 0;
    }
}
