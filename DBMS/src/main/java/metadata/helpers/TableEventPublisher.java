package metadata.helpers;

import metadata.interfaces.MetadataChangeListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TableEventPublisher {
    private final List<MetadataChangeListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(MetadataChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(MetadataChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void notifyListeners(String eventType, String targetName) {
        for (MetadataChangeListener listener : listeners) {
            if (listener != null) {
                listener.onMetadataChanged(eventType, targetName);
            }
        }
    }
}
