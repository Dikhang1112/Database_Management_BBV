package metadata;

import metadata.interfaces.MetadataChangeListener;
import java.util.UUID;

/**
 * Class View đóng vai trò Observer Subscriber lắng nghe sự kiện thay đổi của Table.
 */
public class View implements MetadataChangeListener {
    private UUID viewId;
    private String viewName;
    private String definition;

    public View() {
        this.viewId = UUID.randomUUID();
    }

    public View(String viewName, String definition) {
        this();
        this.viewName = viewName;
        this.definition = definition;
    }

    public UUID getViewId() {
        return viewId;
    }

    public String getViewName() {
        return viewName;
    }

    public String getDefinition() {
        return definition;
    }

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
