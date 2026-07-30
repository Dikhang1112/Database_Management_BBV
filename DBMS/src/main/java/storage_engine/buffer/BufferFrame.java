package storage_engine.buffer;

import storage_engine.abstracts.Page;
import storage_engine.enums.BufferFrameState;

public class BufferFrame {

    private Page page;
    private BufferFrameState state = BufferFrameState.CLEAN;
    private int pinCount = 0;

    public BufferFrame() {
    }

    public BufferFrame(Page page) {
        this.page = page;
    }

    public void pin() {
        this.pinCount++;
        this.state = BufferFrameState.PINNED;
        System.out.println("  -> [State Pattern] Chuyển trạng thái: PINNED (PinCount = " + pinCount + ")");
    }

    public void unpin() {
        if (this.pinCount > 0) {
            this.pinCount--;
        }
        if (this.pinCount == 0) {
            this.state = BufferFrameState.UNPINNED;
            System.out.println("  -> [State Pattern] Chuyển trạng thái: UNPINNED (PinCount = 0 - Sẵn sàng giải phóng)");
        } else {
            System.out.println("  -> [State Pattern] Giảm PinCount = " + pinCount);
        }
    }

    public void markDirty() {
        this.state = BufferFrameState.DIRTY;
        System.out.println("  -> [State Pattern] Chuyển trạng thái: DIRTY (Trang đã bị sửa đổi trong RAM, cần Flush)");
    }

    public void setState(BufferFrameState state) {
        this.state = state;
        System.out.println("  -> [State Pattern] Chuyển trạng thái trực tiếp: " + state);
    }

    public BufferFrameState getState() {
        return state;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getPinCount() {
        return pinCount;
    }
}
