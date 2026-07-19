```mermaid
classDiagram
    direction TB
    %% --- CORE INTERFACES ---
    class MetadataEntity {
        <<interface>>
        +getObjectID() int
        %% Retrieves the physical schema string name for SQL string matching
        +getObjectName() String
    }
    note for MetadataEntity " Base identity blueprint for all structural system metadata entities"

    class DdlCompiler {
        <<interface>>
        %% Parses a raw DDL string statement and builds a concrete metadata object tree
        +compileDdl(String sqlText) MetadataEntity
    }
    note for DdlCompiler " Compiler subsystem interface for translating structural SQL commands"

    class MetadataCache {
        <<interface>>
        +put(int objectID, MetadataEntity obj) void
        +get(int objectID) MetadataEntity
        +invalidate(int objectID) void
    }
    note for MetadataCache " Unified storage abstraction registry interface for metadata in RAM"

    class EvictionStrategy {
        <<interface>>
        %% Evaluates the memory map and evicts the least useful element based on algorithms
        +evict(Map~Integer,MetadataEntity~ cacheMap) int
    }
    note for EvictionStrategy " Algorithmic memory-release contract triggered when hardware capacity limit is reached"

    %% --- CONCRETE IMPLEMENTATION CLASSES ---
    class MetadataDdlCompiler {
        %% Pointer used to push newly constructed schema components into the global RAM cache
        -ObjectMetadataCache cacheReference
        %% Global serial counter supplying unique primary key IDs to new system entities
        -int currentAutoIncrementID
    }
    note for MetadataDdlCompiler " Concrete structural builder converting text commands into live schema components"

    class ObjectMetadataCache {
        %% Primary internal storage indexing active structural entities by their numeric IDs
        -Map~Integer,MetadataEntity~ activeCacheMap
        %% Secondary translation directory mapping string names to numeric IDs for parser verification
        -Map~String,Integer~ nameToIdLookupMap
        %% Tracks the current total count of schema elements buffered in memory
        -int currentCacheSize
        %% Hardware threshold configuration defining the maximum allowable buffered entities
        -int maxCacheCapacity
        %% Reference to the active clearing strategy algorithm injected into the system cache
        -EvictionStrategy currentStrategy
        %% Resolves string-based queries by looking up the name index first, then fetching the object
        +getByObjectName(String objName) MetadataEntity
    }
    note for ObjectMetadataCache " Global main-memory registry optimizing system catalog lookups at runtime"

    class LruEviction {
        %% Sequential historical list tracking accessed object IDs chronologically to find the oldest node
        -List~Integer~ accessHistory
    }
    note for LruEviction " Concrete memory clearing strategy applying Least Recently Used eviction algorithm"

    class LfuEviction {
        %% Frequency counter mapping hitting hits to specific IDs to identify and purge cold schema nodes
        -Map~Integer,Integer~ frequencyMap
    }
    note for LfuEviction " Concrete memory clearing strategy applying Least Frequently Used eviction algorithm"

    %% --- UML RELATIONSHIPS WITH EXPLANATIONS ---
    DdlCompiler <|.. MetadataDdlCompiler : Realizes DDL statement compilation logic
    MetadataCache <|.. ObjectMetadataCache : Realizes main-memory system schema caching
    EvictionStrategy <|.. LruEviction : Realizes time-based stale cache cleanup strategy
    EvictionStrategy <|.. LfuEviction : Realizes hit-count based cold cache cleanup strategy

    MetadataDdlCompiler "1" --> "1" ObjectMetadataCache : pushes newly compiled structural nodes
    ObjectMetadataCache "1" *-- "1" EvictionStrategy : delegates threshold-driven cache clearing
    ObjectMetadataCache "1" o-- "*" MetadataEntity : buffers reference pointers to active schema entities
    MetadataDdlCompiler ..> MetadataEntity : builds physical entities at runtime
```
