package services;

import entity.metadata.domain.Index;
import org.springframework.stereotype.Service;
import repositories.IndexRepository;

/**
 * Service handling business logic for Index operations (Strategy Pattern Rebuild, Disable Index).
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class IndexService {

    private final IndexRepository indexRepository;

    public IndexService(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    public Index findIndex(String dbName, String schemaName, String tableName, String indexName) {
        return indexRepository.findIndex(dbName, schemaName, tableName, indexName);
    }

    /**
     * Strategy Pattern: Executes Index Rebuild using the configured IndexRebuildStrategy and persists changes to index.json.
     */
    public void rebuildIndex(String dbName, String schemaName, String tableName, String indexName) {
        Index index = indexRepository.findIndex(dbName, schemaName, tableName, indexName);
        if (index == null) {
            throw new IllegalArgumentException("Index '" + indexName + "' does not exist");
        }

        // Execute Strategy Pattern algorithm
        index.rebuild();

        // Persist memory state to index.json
        indexRepository.saveIndexes();
    }

    /**
     * Disables an Index and persists changes to index.json.
     */
    public void disableIndex(String dbName, String schemaName, String tableName, String indexName) {
        Index index = indexRepository.findIndex(dbName, schemaName, tableName, indexName);
        if (index == null) {
            throw new IllegalArgumentException("Index '" + indexName + "' does not exist");
        }

        index.disable();
        indexRepository.saveIndexes();
    }
}
