package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.List;

/**
 * Repository đóng vai trò như một Persistence Storage Manager đọc và ghi dữ liệu Metadata
 * từ file JSON (src/data/metadata.json) xuống bộ nhớ RAM (CatalogManager Singleton Domain).
 */
@Repository
public class MetadataModuleRepository {

    private final String jsonFilePath = "src/data/metadata.json";
    private final ObjectMapper objectMapper;

    public MetadataModuleRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Tự động khởi tạo và nạp dữ liệu từ file metadata.json vào CatalogManager Singleton khi Bean được tạo.
     */
    @PostConstruct
    public void init() {
        loadCatalogManager();
    }

    /**
     * Đọc file metadata.json và nạp đối tượng vào CatalogManager Singleton.
     *
     * @return Đối tượng CatalogManager sau khi nạp dữ liệu từ file JSON.
     */
    public synchronized CatalogManager loadCatalogManager() {
        CatalogManager catalogManager = CatalogManager.getInstance();
        catalogManager.clear();

        File file = new File(jsonFilePath);
        if (!file.exists()) {
            // Trường hợp file không tồn tại ở working dir tương đối, thử tìm từ thư mục dự án gốc
            file = Paths.get("DBMS", "src", "data", "metadata.json").toFile();
        }

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
                    if ("OFFLINE".equalsIgnoreCase(statusStr)) {
                        database.setStatus(DatabaseStatus.OFFLINE);
                    }

                    JsonNode schemasNode = dbNode.path("schemas");
                    if (schemasNode.isArray()) {
                        for (JsonNode schemaNode : schemasNode) {
                            String schemaName = schemaNode.path("schemaName").asText();
                            if (schemaName == null || schemaName.isBlank()) continue;

                            Schema schema = database.createSchema(schemaName);
                            boolean readOnly = schemaNode.path("readOnly").asBoolean(false);
                            schema.setReadOnly(readOnly);

                            JsonNode tablesNode = schemaNode.path("tables");
                            if (tablesNode.isArray()) {
                                for (JsonNode tableNode : tablesNode) {
                                    String tableName = tableNode.path("tableName").asText();
                                    if (tableName == null || tableName.isBlank()) continue;

                                    Table table = schema.createTable(tableName);
                                    boolean locked = tableNode.path("locked").asBoolean(false);
                                    table.setLocked(locked);

                                    // Nạp danh sách cột
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

                                    // Nạp danh sách Index
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
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file metadata.json: " + e.getMessage());
        }

        return catalogManager;
    }

    /**
     * Lấy tham chiếu tới CatalogManager Singleton trong RAM.
     */
    public CatalogManager getCatalogManager() {
        return CatalogManager.getInstance();
    }

    /**
     * Lấy tham chiếu tới MetadataModule Facade Singleton.
     */
    public MetadataModule getMetadataModule() {
        return MetadataModule.getInstance();
    }
}
