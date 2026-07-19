classDiagram
    direction LR

    %% =========================================================
    %% PHYSICAL STORAGE LAYER
    %% =========================================================

    class FileManager {
        +readPage(PageId id) DataPage
        +writePage(DataPage page) void
        +allocatePage() PageId
        +deallocatePage(PageId id) void
    }

    class PageManager {
        +createPage() DataPage
        +formatPage(DataPage page) void
        +splitPage(DataPage page) void
        +mergePage(DataPage page) void
    }

    class PageFactory {
        +createHeapPage() DataPage
        +createIndexPage() DataPage
        +createOverflowPage() DataPage
    }

    class DataPage {
        -PageId pageId
        -PageHeader header
        -SlotDirectory slotDirectory
        -Tuple[] tuples
        +insertRecord() void
        +deleteRecord() void
        +updateRecord() void
        +readRecord() void
    }

    class PageHeader {
        -int pageSize
        -int freeSpace
        -PageType pageType
    }

    class SlotDirectory {
        -Slot[] slots
    }

    class Tuple {
        -RID rid
        -byte[] payload
    }

    class PageId {
        <<Value Object>>
    }

    class RID {
        <<Value Object>>
    }

    class PageType {
        <<enumeration>>
    }

    %% =========================================================
    %% BUFFER POOL LAYER
    %% =========================================================

    class BufferPoolManager {
        -BufferFrame[] frames
        -PageReplacer replacer
        -FileManager fileManager
        -Map<Integer,DataPage> pageCacheMap
        +fetchPage(PageId id) DataPage
        +newPage() DataPage
        +flushPage(PageId id) void
        +pinPage(PageId id) void
        +unpinPage(PageId id) void
    }

    class BufferFrame {
        -DataPage page
        -boolean dirty
        -int pinCount
    }

    class BufferPool {
        -BufferFrame[] frames
    }

    class PageReplacer {
        <<Interface>>
        +selectVictimPage() BufferFrame
    }

    class LRUPageReplacer {
    }

    class ClockPageReplacer {
    }

    %% =========================================================
    %% TRANSACTION CONCURRENCY
    %% =========================================================

    class TransactionManager {
        +begin() void
        +commit() void
        +rollback() void
    }

    class Transaction {
        -TransactionId transactionId
        -TransactionState state
    }

    class TransactionId {
        <<Value Object>>
    }

    class TransactionState {
        <<enumeration>>
    }

    class LockManager {
        -LockTable lockTable
        +acquireLock() void
        +releaseLock() void
    }

    class LockTable {
        -Map<ResourceId,LockRequest[]> locks
    }

    class LockRequest {
        -Transaction transaction
        -LockMode mode
        -boolean granted
    }

    class LockMode {
        <<enumeration>>
    }

    class ResourceId {
        <<Value Object>>
    }

    %% =========================================================
    %% RELATIONSHIPS
    %% =========================================================

    FileManager --> PageManager
    PageManager --> PageFactory
    PageFactory --> DataPage

    DataPage *-- PageHeader
    DataPage *-- SlotDirectory
    DataPage *-- Tuple

    BufferPoolManager --> FileManager
    BufferPoolManager --> BufferPool
    BufferPoolManager --> PageReplacer

    BufferPool *-- BufferFrame
    BufferFrame *-- DataPage

    PageReplacer <|.. LRUPageReplacer
    PageReplacer <|.. ClockPageReplacer

    TransactionManager --> Transaction

    LockManager --> LockTable
    LockTable *-- LockRequest

    LockRequest --> Transaction
    LockRequest --> LockMode