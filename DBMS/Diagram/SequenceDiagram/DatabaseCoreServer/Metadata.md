```mermaid
sequenceDiagram
    autonumber
    
    %% --- FIXED ARCHITECTURAL OBJECTS (TOP ROW ONLY) ---
    actor User as User
    participant EX as ExecutionEngine
    participant DC as MetadataDdlCompiler
    participant MC as ObjectMetadataCache
    participant EVC as LruEviction
    participant TBL as Table

    %% ==========================================
    %% --- SCENARIO A: DDL EXECUTION (CREATE TABLE) ---
    %% ==========================================
    Note over User, TBL: SCENARIO A: COMPILING & CACHING NEW METADATA (e.g., CREATE TABLE)
    User->>EX: Send DDL request ("CREATE TABLE customers...")
    EX->>DC: compileDdl("CREATE TABLE customers...")
    activate DC
    
    Note over DC:<br/>Assign a unique int ID (e.g., 301)
    DC->>TBL: Instantiates physical Table object (tableID=301, name="customers")
    
    DC->>MC: put(301, tableInstance)
    activate MC
    
    %% Cache capacity check loop
    alt currentCacheSize >= maxCacheCapacity
        Note over MC: Free up RAM when you run out of space <br/> when hardware capacity threshold reached! <br/>
        MC->>EVC: evict(activeCacheMap)
        activate EVC
        EVC-->>MC: Return expiredObjectID (int)
        deactivate EVC
    end
    
    Note over MC: Maps unique ID in activeCacheMap <br/>and maps "customers" -> 301 in nameToIdLookupMap
    MC-->>DC: Return caching status (SUCCESS)
    deactivate MC
    
    DC-->>EX: Return newly minted MetadataEntity reference
    deactivate DC
    EX-->>User: Send DDL Success Packet

    %% ==========================================
    %% --- SCENARIO B: DML EXECUTION (INSERT INTO TABLE) ---
    %% ==========================================
    Note over User, TBL: SCENARIO B: HIGH-SPEED CACHE LOOKUP DURING DML (e.g., INSERT INTO)
    User->>EX: Send DML request ("INSERT INTO customers VALUES (...)")
    
    EX->>MC: getByObjectName("customers")
    activate MC
    Note over MC: Lookup <br/>on nameToIdLookupMap to translate "customers" -> 301
    MC->>MC: get(301) -- Fetches active schema pointer from activeCacheMap
    MC-->>EX: Return MetadataEntity pointer (Table instance)
    deactivate MC
    
    Note over EX: Execution Engine safely unboxes the metadata entity
    
    EX->>TBL: validateRow(row)
    activate TBL
    TBL-->>EX: Return isValid (true)
    deactivate TBL
    
    EX-->>User: Send DML Success Packet ("Query OK, 1 row affected")
```
