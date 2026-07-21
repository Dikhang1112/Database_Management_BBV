package metadata;

import metadata.interfaces.MetadataChangeListener;

/**
 * Class View đóng vai trò Observer Subscriber lắng nghe sự kiện thay đổi của Table.
 */
public class View implements MetadataChangeListener {

    // Pattern: Observer (Subscriber)
    @Override
    public void onMetadataChanged(String eventType, String targetName) {
        compile();
    }

    // Pattern: Observer (Subscriber)
    public void compile() {
    }

    // Pattern: Observer (Subscriber)
    public void refresh() {
    }

    public void updateDefinition(String sql) {
    }
}
