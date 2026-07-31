package dto;

import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;

import java.util.List;

public class CatalogManagerDTO {
    private final String elementName;
    private final String elementType;
    private final int totalDatabases;
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
