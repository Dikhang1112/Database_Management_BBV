sequenceDiagram
    autonumber
    
    %% --- FIXED COMPONENT DECLARATIONS (TOP ROW ONLY) ---
    participant EX as ExecutionEngine
    participant LM as LockManager
    participant BPM as BufferPoolManager
    participant RE as LruPageReplacer
    participant FM as DiskFileManager
    participant PG as DataPage

    %% ========================================================
    %% PHASE 1: CONCURRENCY CONTROL (ACQUIRING SYSTEM LOCK)
    %% ========================================================
    Note over EX, LM: PHASE 1: TRANSACTIONAL CONCURRENCY CONTROL
    EX->>LM: acquireLock(pageID=99, txID=7, lockMode="X")
    activate LM
    Note over LM: Creates and logs internal LockRequest token
    LM-->>EX: Return isGranted (true)
    deactivate LM

    %% ========================================================
    %% PHASE 2: BUFFER POOL MANAGEMENT (RAM CACHE VALIDATION)
    %% ========================================================
    Note over EX, RE: PHASE 2: BUFFER POOL & EVICTION MANAGEMENT
    EX->>BPM: fetchPage(pageID=99)
    activate BPM
    
    alt Cache Miss (No has Page 99 in Ram)        
        opt If RAM Cache is Full
            BPM->>RE: selectVictimPage(pageCacheMap)
            activate RE
            Note over RE: Remove least recently used page from LRU registry
            RE-->>BPM: Return victimPageID (e.g., 12)
            deactivate RE
            
            BPM->>FM: writePageToDisk(pageID=12, pageData)
            activate FM
            Note over FM: Overwrites dirty binary block back to OS disk directory
            FM-->>BPM: Write Status (SUCCESS)
            deactivate FM
            BPM->>BPM: Remove page 12 from pageCacheMap
        end

        %% ========================================================
        %% PHASE 3: PHYSICAL DISK PHYSICAL STORAGE I/O
        %% ========================================================
        Note over BPM, PG: PHASE 3: PHYSICAL STORAGE READ 
        BPM->>FM: readPageFromDisk(pageID=99)
        activate FM
        FM-->>BPM: Return raw binary byte array (byte[])
        deactivate FM
        
        Note over BPM, PG: Create and Initialize DataPage instance
        BPM->>PG: Initialize(pageID=99, binaryBuffer)
        activate PG
        PG-->>BPM: Instance initialized
        deactivate PG
        BPM->>BPM: Cache new page inside pageCacheMap registry
    end

    BPM-->>EX: Return loaded DataPage reference (High-speed pointer)
    deactivate BPM

    %% Data Mutation Event inside Execution Engine
    EX->>PG: getRecord(slotNumber=3)
    activate PG
    PG-->>EX: Return record bytes (byte[])
    deactivate PG
