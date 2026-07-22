package metadata.helpers;

import metadata.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SchemaManager {
    private final Map<String, Schema> schemas = new ConcurrentHashMap<>();

    public Schema add(String schemaName) {
        SecurityValidator.validatePermission(schemaName);
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        if (contains(schemaName)) {
            throw new IllegalStateException("Schema already exists");
        }
        Schema schema = new Schema(schemaName);
        schemas.put(schemaName.toLowerCase(), schema);
        return schema;
    }

    public void remove(String schemaName) {
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        if (!contains(schemaName)) {
            throw new IllegalArgumentException("Schema not found");
        }
        schemas.remove(schemaName.toLowerCase());
    }

    public Schema get(String schemaName) {
        if (schemaName == null) return null;
        return schemas.get(schemaName.toLowerCase());
    }

    public boolean contains(String schemaName) {
        if (schemaName == null) return false;
        return schemas.containsKey(schemaName.toLowerCase());
    }

    public void rename(String oldName, String newName) {
        CatalogValidator.validateIdentifier(oldName, "Schema");
        CatalogValidator.validateIdentifier(newName, "Schema");
        if (!contains(oldName)) {
            throw new IllegalArgumentException("Schema not found");
        }
        if (contains(newName) && !oldName.equalsIgnoreCase(newName)) {
            throw new IllegalStateException("Schema already exists");
        }
        Schema schema = schemas.remove(oldName.toLowerCase());
        schema.rename(newName);
        schemas.put(newName.toLowerCase(), schema);
    }

    public List<Schema> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(schemas.values()));
    }
}
