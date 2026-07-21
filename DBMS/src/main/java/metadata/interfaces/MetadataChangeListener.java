package metadata.interfaces;

/**
 * Interface cho Observer Pattern trong metadata module.
 * Nhận thông báo khi có sự thay đổi cấu trúc Metadata (TABLE_ALTERED, COLUMN_REMOVED, etc.).
 */
public interface MetadataChangeListener {
    void onMetadataChanged(String eventType, String targetName);
}
