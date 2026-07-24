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
        CatalogValidator.ensureUniqueName(schemaName, schemas.keySet(), "Schema");
        Schema schema = new Schema(schemaName);
        schemas.put(schemaName.toLowerCase(), schema);
        return schema;
    }

    public void remove(String schemaName) {
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        CatalogValidator.ensureExists(schemaName, schemas.keySet(), "Schema");
        schemas.remove(schemaName.toLowerCase());
    }

    public Schema get(String schemaName) {
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        CatalogValidator.ensureExists(schemaName, schemas.keySet(), "Schema");
        return schemas.get(schemaName.toLowerCase());
    }

    public boolean contains(String schemaName) {
        if (schemaName == null) return false;
        return schemas.containsKey(schemaName.toLowerCase());
    }

    public void rename(String oldName, String newName) {
        CatalogValidator.validateIdentifier(oldName, "Schema");
        CatalogValidator.validateIdentifier(newName, "Schema");
        CatalogValidator.ensureExists(oldName, schemas.keySet(), "Schema");
        CatalogValidator.ensureUniqueName(newName, schemas.keySet(), "Schema");
        Schema schema = schemas.remove(oldName.toLowerCase());
        schema.rename(newName);
        schemas.put(newName.toLowerCase(), schema);
    }

    public List<Schema> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(schemas.values()));
    }
}
