package services;

import dto.CatalogManagerDTO;
import dto.DDLRequestDTO;
import entity.metadata.domain.CatalogManager;
import entity.metadata.facade.MetadataModule;
import org.springframework.stereotype.Service;
import repositories.MetadataModuleRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Service handling business logic for Metadata Subsystem operations (Facade, Singleton, Command Patterns).
 * Communicates with MetadataModuleRepository to persist changes to metadata.json.
 */
@Service
public class MetadataService {

    private final MetadataModuleRepository metadataModuleRepository;

    public MetadataService(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
    }

    /**
     * 1. Singleton Pattern: Simulates and verifies the Double-Checked Locking (DCL) Singleton instance.
     */
    public Map<String, Object> GetInstance() {
        MetadataModule call1 = MetadataModule.getInstance();
        int hashCode1 = System.identityHashCode(call1);

        MetadataModule call2 = MetadataModule.getInstance();
        int hashCode2 = System.identityHashCode(call2);

        boolean isSameObject = (call1 == call2);

        Map<String, Object> result = new HashMap<>();
        result.put("targetClass", "entity.metadata.facade.MetadataModule");
        result.put("factoryMethod", "getInstance()");
        result.put("firstCallHashCode", "0x" + Integer.toHexString(hashCode1).toUpperCase());
        result.put("secondCallHashCode", "0x" + Integer.toHexString(hashCode2).toUpperCase());
        result.put("isSameInstance", isSameObject);
        result.put("singletonPatternStatus", isSameObject ? "VERIFIED (Double-Checked Locking Singleton Verified)" : "FAILED");
        result.put("hasCatalogManager", call1.getCatalogManager() != null);

        return result;
    }

    /**
     * 2. Facade Pattern: Retrieves the root CatalogManager via MetadataModule Facade.
     */
    public CatalogManagerDTO getCatalogManager() {
        CatalogManager catalogManager = metadataModuleRepository.getCatalogManager();
        return new CatalogManagerDTO(catalogManager);
    }

    /**
     * 3. Command Pattern: Encapsulates and executes DDL Commands and persists state to metadata.json.
     */
    public void executeDDL(DDLRequestDTO request) {
        if (request == null || request.getCommandType() == null) {
            throw new IllegalArgumentException("DDL Request CommandType cannot be null");
        }

        String commandType = request.getCommandType();
        String dbName = request.getDatabaseName();

        if ("CREATE_DATABASE".equalsIgnoreCase(commandType) && dbName != null) {
            metadataModuleRepository.getCatalogManager().createDatabase(dbName);
        } else if ("DROP_DATABASE".equalsIgnoreCase(commandType) && dbName != null) {
            metadataModuleRepository.getCatalogManager().dropDatabase(dbName);
        }

        // Persist memory state to metadata.json
        metadataModuleRepository.saveCatalogManager();
    }
}
