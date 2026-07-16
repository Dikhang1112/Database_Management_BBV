```mermaid
classDiagram
    direction LR
    %% ==========================================
    %% LAYER 1: PHYSICAL STORAGE
    %% ==========================================
    class DiskFileManager {
        -String dataDirectoryPath
        +readPageFromDisk(int pageID) byte[]
        +writePageToDisk(int pageID, byte[] pageData) void
    }

    class DataPage {
        -int pageID [PK]
        -int tableID [FK]
        -int slotCount
        -byte[] binaryBuffer
        +insertRecord(byte[] recordBytes) boolean
        +getRecord(int slotNumber) byte[]
    }

    %% ==========================================
    %% LAYER 2: BUFFER POOL MANAGEMENT
    %% ==========================================
    class BufferPoolManager {
        -Map~Integer,DataPage~ pageCacheMap
        -int poolCapacity
        -PageReplacer currentReplacer
        -DiskFileManager fileManagerReference
        +fetchPage(int pageID) DataPage
        +flushPage(int pageID) void
    }

    class PageReplacer {
        <<interface>>
        +selectVictimPage(Map~Integer,DataPage~ cacheMap) int
    }

    class LruPageReplacer {
        -List~Integer~ lruList
    }

    %% ==========================================
    %% LAYER 3: TRANSACTION CONCURRENCY
    %% ==========================================
    class LockManager {
        -Map~Integer,List~LockRequest~~ lockTable
        +acquireLock(int resourceID, int txID, String lockMode) boolean
        +releaseLock(int resourceID, int txID) void
    }

    class LockRequest {
        -int transactionID
        -String lockMode
        -boolean isGranted
    }

    %% ==========================================
    %% CORE UML REALIZATIONS & CONNECTIONS
    %% ==========================================
    PageReplacer <|.. LruPageReplacer : implements swap strategy
    LockManager "1" *-- "*" LockRequest : tracks active tokens
    BufferPoolManager "1" o-- "*" DataPage : buffers active frames
```
