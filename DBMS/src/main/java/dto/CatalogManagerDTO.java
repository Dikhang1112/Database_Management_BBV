package dto;

import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CatalogManagerDTO {

    @Schema(description = "Tên thành phần Metadata Element", example = "CatalogManager")
    private final String elementName;

    @Schema(description = "Loại thành phần Metadata", example = "CatalogManager")
    private final String elementType;

    @Schema(description = "Tổng số Database trong Catalog", example = "2")
    private final int totalDatabases;

    @Schema(description = "Danh sách các Database hiện có", example = "[\"sales_db\", \"inventory_db\"]")
    private final List<String> databases;

    public CatalogManagerDTO(CatalogManager catalogManager) {
        this.elementName = catalogManager.getElementName();
        this.elementType = catalogManager.getElementType();
        List<Database> dbs = catalogManager.listDatabases();
        this.totalDatabases = dbs != null ? dbs.size() : 0;
        this.databases = dbs != null ? dbs.stream().map(Database::getElementName).toList() : List.of();
    }

    public String getElementName() {
        return elementName;
    }

    public String getElementType() {
        return elementType;
    }

    public int getTotalDatabases() {
        return totalDatabases;
    }

    public List<String> getDatabases() {
        return databases;
    }
}
