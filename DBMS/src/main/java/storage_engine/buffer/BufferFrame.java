package storage_engine.buffer;

import storage_engine.abstracts.Page;
import storage_engine.enums.BufferFrameState;

public class BufferFrame {

    public void pin() {
    }

    public void unpin() {
    }

    public void markDirty() {
    }

    public void setState(BufferFrameState state) {
    }

    public BufferFrameState getState() {
        return null;
    }

    public Page getPage() {
        return null;
    }
}
