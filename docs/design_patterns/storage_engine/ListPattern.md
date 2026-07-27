# Design Patterns in Storage Engine Module

This document details the **core and subsystem Design Patterns** applied in the `storage_engine` module, organized by GoF pattern classification: **Creational Patterns** (Create) ➔ **Structural Patterns** (Structure) ➔ **Behavioral Patterns** (Behavior).

---

## 1. Creational Patterns (Create)

### 1.1. Singleton Pattern
* **Pattern**: Singleton Pattern
* **Class/Interface Applied**: `BufferPoolManager`
* **Method**: `getInstance()`
* **Reason for Use**: Ensures only a single instance of `BufferPoolManager` exists in memory to manage all buffer frames, page tables, and dirty page flushes centrally without concurrency state conflicts.

### 1.2. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: `PageFactory`
* **Method**: `createPage(pageType)`
* **Reason for Use**: Encapsulates the instantiation logic for different page types (`DataPage`, `IndexPage`, `CatalogPage`), allowing new page formats to be introduced without modifying storage engine core callers.

### 1.3. Builder Pattern
* **Pattern**: Builder Pattern
* **Class/Interface Applied**: `PageBuilder`
* **Method**: `buildHeader()`, `buildSlots()`, `buildRecords()`, `build()`
* **Reason for Use**: Constructs complex slotted-page structures step-by-step by initializing page headers, allocating slot directories, and packing data records cleanly before page serialization.

---

## 2. Structural Patterns (Structure)

### 2.1. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: `StorageEngine`
* **Method**: `fetchPage(pageId)`, `allocatePage()`, `freePage()`
* **Reason for Use**: Provides a unified, high-level API entry point for external modules (Execution Engine, Query Processor) to interact with physical storage, hiding the complex coordination between `BufferPoolManager`, `PageFactory`, and `DiskFileManager`.

### 2.2. Adapter Pattern
* **Pattern**: Adapter Pattern
* **Class/Interface Applied**: `StorageAdapter` (Interface implemented by `LocalDiskAdapter`, `MemoryStorageAdapter`, `CloudStorageAdapter`)
* **Method**: `read(pageId)`, `write(page)`, `flush()`
* **Reason for Use**: Standardizes disk and memory I/O operations across different storage backends (Local FileSystem, RAM Mock, S3/Cloud Storage) so `DiskFileManager` can seamlessly swap underlying storage media.

### 2.3. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: `BTreeNode` (Abstract base for `InternalNode` and `LeafNode`)
* **Method**: `search()`, `insert()`, `split()`
* **Reason for Use**: Models the hierarchical B+Tree index tree uniformly, allowing internal routing nodes and leaf data entry nodes to be treated through a single unified `BTreeNode` abstraction.

---

## 3. Behavioral Patterns (Behavior)

### 3.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: `PageReplacementStrategy` (Interface implemented by `LRUReplacementStrategy`, `ClockReplacementStrategy`, `FIFOReplacementStrategy`), `ReplacementContext`
* **Method**: `selectVictim()`, `setStrategy()`, `evict()`
* **Reason for Use**: Encapsulates page eviction algorithms into interchangeable strategy objects, allowing `BufferPoolManager` to dynamically switch eviction policies (LRU, Clock, FIFO) based on workload characteristics.

### 3.2. State Pattern
* **Pattern**: State Pattern
* **Class/Interface Applied**: `BufferFrame`, `BufferFrameState` (Enum: `CLEAN`, `DIRTY`, `PINNED`, `UNPINNED`)
* **Method**: `pin()`, `unpin()`, `markDirty()`, `setState()`
* **Reason for Use**: Manages the dynamic lifecycle and state transitions of individual buffer frames, preventing dirty frames from being evicted prematurely and enforcing pin/unpin locking rules.

### 3.3. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface Applied**: `Page` (Abstract Class), `DiskFileManager`
* **Method**: `read()`, `deserialize()`, `modify()`, `serialize()`, `write()`
* **Reason for Use**: Defines the skeleton workflow for page I/O and record manipulation in `Page`, delegating concrete serialization and slot placement steps to specialized subclasses (`DataPage`, `IndexPage`, `CatalogPage`).

### 3.4. Iterator Pattern
* **Pattern**: Iterator Pattern
* **Class/Interface Applied**: `PageIterator`, `BTreeIterator`
* **Method**: `hasNext()`, `next()`
* **Reason for Use**: Provides sequential, row-by-row data and index traversal over page slots and B+Tree leaf nodes without exposing internal page memory representations.
