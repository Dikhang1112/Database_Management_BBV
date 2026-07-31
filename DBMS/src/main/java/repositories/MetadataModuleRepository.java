package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.constraints.ForeignKeyConstraint;
import entity.metadata.constraints.PrimaryKeyConstraint;
import entity.metadata.domain.*;
import entity.metadata.enums.DataType;
import entity.metadata.enums.DatabaseStatus;
import entity.metadata.enums.IndexType;
import entity.metadata.facade.MetadataModule;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Persistence Storage Repository reading and writing Metadata between 
 * JSON files (src/data/metadata/metadata.json) and RAM (CatalogManager Singleton Domain).
 */
@Repository
public class MetadataModuleRepository {

    private final String primaryJsonPath = "src/data/metadata/metadata.json";
    private final ObjectMapper objectMapper;

    public MetadataModuleRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Automatically loads metadata from JSON file into CatalogManager Singleton upon Bean initialization.
     */
    @PostConstruct
    public void init() {
        loadCatalogManager();
    }

    private File getMetadataFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "metadata.json").toFile();
        }
        if (!file.exists()) {
            file = new File("src/data/metadata.json");
        }
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata.json").toFile();
        }
        return file;
    }

    /**
     * Loads JSON file and populates the CatalogManager Singleton instance.
     *
     * @return CatalogManager Singleton instance populated with domain objects.
     */
    public synchronized CatalogManager loadCatalogManager() {
        CatalogManager catalogManager = CatalogManager.getInstance();
        catalogManager.clear();

        File file = getMetadataFile();
        if (!file.exists()) {
            return catalogManager;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode databasesNode = rootNode.path("databases");

            if (databasesNode.isArray()) {
                for (JsonNode dbNode : databasesNode) {
                    String dbName = dbNode.path("databaseName").asText();
                    if (dbName == null || dbName.isBlank()) continue;

                    Database database = catalogManager.createDatabase(dbName);
                    String statusStr = dbNode.path("status").asText("ONLINE");

                    JsonNode schemasNode = dbNode.path("schemas");
                    if (schemasNode.isArray()) {
                        for (JsonNode schemaNode : schemasNode) {
                            String schemaName = schemaNode.path("schemaName").asText();
                            if (schemaName == null || schemaName.isBlank()) continue;

                            Schema schema = database.createSchema(schemaName);
                            boolean readOnly = schemaNode.path("readOnly").asBoolean(false);

                            JsonNode tablesNode = schemaNode.path("tables");
                            if (tablesNode.isArray()) {
                                for (JsonNode tableNode : tablesNode) {
                                    String tableName = tableNode.path("tableName").asText();
                                    if (tableName == null || tableName.isBlank()) continue;

                                    Table table = schema.createTable(tableName);
                                    boolean locked = tableNode.path("locked").asBoolean(false);

                                    // 1. Columns Parsing
                                    JsonNode columnsNode = tableNode.path("columns");
                                    if (columnsNode.isArray()) {
                                        for (JsonNode colNode : columnsNode) {
                                            String colName = colNode.path("columnName").asText();
                                            String dataTypeStr = colNode.path("dataType").asText("VARCHAR");
                                            DataType dataTypeEnum;
                                            try {
                                                dataTypeEnum = DataType.valueOf(dataTypeStr.toUpperCase());
                                            } catch (IllegalArgumentException e) {
                                                dataTypeEnum = DataType.VARCHAR;
                                            }
                                            boolean isNullable = colNode.path("isNullable").asBoolean(true);
                                            String defaultValue = colNode.path("defaultValue").isNull() ? null : colNode.path("defaultValue").asText();

                                            Column column = new Column(colName, dataTypeEnum);
                                            column.setNullable(isNullable);
                                            column.setDefaultValue(defaultValue);
                                            table.addColumn(column);
                                        }
                                    }

                                    // 2. Constraints Parsing
                                    JsonNode constraintsNode = tableNode.path("constraints");
                                    if (constraintsNode.isArray()) {
                                        for (JsonNode cNode : constraintsNode) {
                                            String cName = cNode.path("constraintName").asText();
                                            String cType = cNode.path("constraintType").asText("PRIMARY_KEY");
                                            boolean enabled = cNode.path("enabled").asBoolean(true);

                                            if ("PRIMARY_KEY".equalsIgnoreCase(cType)) {
                                                PrimaryKeyConstraint pk = new PrimaryKeyConstraint(cName);
                                                if (!enabled) pk.disable();
                                                table.addConstraint(pk);
                                            } else if ("FOREIGN_KEY".equalsIgnoreCase(cType)) {
                                                String refTable = cNode.path("referencedTable").asText();
                                                String refCol = cNode.path("referencedColumn").asText();
                                                ForeignKeyConstraint fk = new ForeignKeyConstraint(cName, refTable, refCol);
                                                if (!enabled) fk.disable();
                                                table.addConstraint(fk);
                                            }
                                        }
                                    }

                                    // 3. Indexes Parsing
                                    JsonNode indexesNode = tableNode.path("indexes");
                                    if (indexesNode.isArray()) {
                                        for (JsonNode idxNode : indexesNode) {
                                            String indexName = idxNode.path("indexName").asText();
                                            String colName = idxNode.path("columnName").asText();
                                            String idxTypeStr = idxNode.path("indexType").asText("BTREE");
                                            IndexType indexType = "HASH".equalsIgnoreCase(idxTypeStr) ? IndexType.HASH : IndexType.BTREE;

                                            Index index = new Index(indexName, colName, indexType);
                                            if (!idxNode.path("enabled").asBoolean(true)) {
                                                index.disable();
                                            }
                                            table.addIndex(index);
                                        }
                                    }

                                    // Set table lock state AFTER adding child elements
                                    table.setLocked(locked);
                                }
                            }

                            // Set schema readOnly state AFTER creating all tables
                            schema.setReadOnly(readOnly);
                        }
                    }

                    // Set database status
                    if ("OFFLINE".equalsIgnoreCase(statusStr)) {
                        database.setStatus(DatabaseStatus.OFFLINE);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading metadata.json: " + e.getMessage());
        }

        return catalogManager;
    }

    /**
     * Persists the live CatalogManager Singleton domain memory state back to metadata.json.
     */
    public synchronized void saveCatalogManager() {
        File file = getMetadataFile();
        try {
            CatalogManager cm = CatalogManager.getInstance();
            objectMapper.writeValue(file, cm);
        } catch (IOException e) {
            System.err.println("Error persisting metadata.json: " + e.getMessage());
        }
    }

    /**
     * Returns the CatalogManager Singleton instance.
     */
    public CatalogManager getCatalogManager() {
        return CatalogManager.getInstance();
    }

    /**
     * Returns the MetadataModule Facade Singleton instance.
     */
    public MetadataModule getMetadataModule() {
        return MetadataModule.getInstance();
    }
}
