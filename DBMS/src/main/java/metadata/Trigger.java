package metadata;

import metadata.interfaces.MetadataChangeListener;

/**
 * Class Trigger đóng vai trò Observer Subscriber lắng nghe sự kiện thay đổi của Table.
 */
public class Trigger implements MetadataChangeListener {

    public void enable() {
    }

    public void disable() {
    }

    // Pattern: Observer (Subscriber)
    @Override
    public void onMetadataChanged(String eventType, String targetName) {
        compile();
    }

    // Pattern: Observer (Subscriber)
    public void compile() {
    }
}
