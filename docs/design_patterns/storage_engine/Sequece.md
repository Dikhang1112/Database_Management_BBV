# Sequence Diagrams for Storage Engine Module (By Pattern & By Feature)

Tài liệu thể hiện các sơ đồ trình tự (Sequence Diagrams) cho module **Storage Engine**, bao gồm 2 phần chính:
- **Phần I: Sơ Đồ Trình Tự Theo Design Pattern** (Theo thứ tự: Creational ➔ Structural ➔ Behavioral).
- **Phần II: Sơ Đồ Trình Tự Theo Feature Cốt Lõi** (Chuỗi tương tác tính năng thực tế).

---

# PHẦN I: SƠ ĐỒ TRÌNH TỰ THEO DESIGN PATTERN

## 1. Creational Patterns (Create)

### 1.1. Singleton Pattern
* **Pattern**: Singleton Pattern
* **Class/Interface Applied**: `BufferPoolManager`
* **Method**: `getInstance()`

```mermaid
sequenceDiagram
    autonumber
    actor Engine as StorageEngine
    participant BPM as BufferPoolManager (Singleton)

    Engine->>+BPM: getInstance()
    alt Instance is null
        BPM->>+BPM: create new BufferPoolManager()
    end
    BPM-->>-Engine: BufferPoolManager instance
```

---

### 1.2. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: `PageFactory`
* **Method**: `createPage(pageType)`

```mermaid
sequenceDiagram
    autonumber
    actor Engine as StorageEngine / BufferPoolManager
    participant Factory as PageFactory (Factory)
    participant Page as Page

    Engine->>+Factory: createPage("DATA_PAGE")
    alt PageType is DATA_PAGE
        Factory->>+Page: instantiate DataPage()
    else PageType is INDEX_PAGE
        Factory->>+Page: instantiate IndexPage()
    else PageType is CATALOG_PAGE
        Factory->>+Page: instantiate CatalogPage()
    end
    Page-->>-Factory: Page instance
    Factory-->>-Engine: Page instance
```

---

### 1.3. Builder Pattern
* **Pattern**: Builder Pattern
* **Class/Interface Applied**: `PageBuilder`
* **Method**: `buildHeader()`, `buildSlots()`, `buildRecords()`, `build()`

```mermaid
sequenceDiagram
    autonumber
    actor Factory as PageFactory / Engine
    participant Builder as PageBuilder (Builder)
    participant Header as PageHeader
    participant Slot as SlotDirectory
    participant Record as Record
    participant Page as DataPage

    Factory->>+Builder: buildHeader(pageId, lsn)
    Builder->>+Header: initializeHeader()
    Header-->>-Builder: Header OK
    Factory->>+Builder: buildSlots(slotCount)
    Builder->>+Slot: allocateSlots()
    Slot-->>-Builder: Slots OK
    Factory->>+Builder: buildRecords(records)
    Builder->>+Record: packRecords()
    Record-->>-Builder: Records OK
    Factory->>+Builder: build()
    Builder->>+Page: instantiate DataPage(Header, Slots, Records)
    Page-->>-Builder: DataPage instance
    Builder-->>-Factory: DataPage instance
```

---

## 2. Structural Patterns (Structure)

### 2.1. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: `StorageEngine`
* **Method**: `fetchPage(pageId)`

```mermaid
sequenceDiagram
    autonumber
    actor Exec as ExecutionEngine
    participant SE as StorageEngine (Facade)
    participant BPM as BufferPoolManager
    participant Disk as DiskFileManager

    Exec->>+SE: fetchPage(101)
    SE->>+BPM: fetchPage(101)
    alt Page is in Buffer Pool (Hit)
        BPM-->>SE: BufferFrame with Page 101
    else Page is NOT in Buffer Pool (Miss)
        BPM->>+Disk: readPage(101)
        Disk-->>-BPM: Page 101 data
        BPM-->>-SE: BufferFrame with Page 101
    end
    SE-->>-Exec: BufferFrame / Page 101
```

---

### 2.2. Adapter Pattern
* **Pattern**: Adapter Pattern
* **Class/Interface Applied**: `StorageAdapter` (implemented by `LocalDiskAdapter`, `MemoryStorageAdapter`, `CloudStorageAdapter`)
* **Method**: `read(pageId)`, `write(page)`, `flush()`

```mermaid
sequenceDiagram
    autonumber
    participant DFM as DiskFileManager
    participant Adapter as StorageAdapter (LocalDiskAdapter)
    participant Disk as Physical Disk / OS File

    DFM->>+Adapter: write(page101)
    Adapter->>+Disk: writeBlockToFile(page101Bytes)
    Disk-->>-Adapter: Write Success
    Adapter-->>-DFM: Write OK

    DFM->>+Adapter: flush()
    Adapter->>+Disk: fsync()
    Disk-->>-Adapter: Flush Success
    Adapter-->>-DFM: Flush OK
```

---

### 2.3. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: `BTreeNode` (Abstract base for `InternalNode` and `LeafNode`)
* **Method**: `search()`, `insert()`, `split()`

```mermaid
sequenceDiagram
    autonumber
    participant Client as ExecutionEngine / IndexManager
    participant Root as BTreeNode (InternalNode)
    participant Child as BTreeNode (LeafNode)

    Client->>+Root: search(searchKey)
    Root->>+Root: findChild(searchKey)
    Root->>+Child: search(searchKey)
    Child-->>-Root: RID / Record Pointer
    Root-->>-Client: RID / Record Pointer
```

---

## 3. Behavioral Patterns (Behavior)

### 3.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: `PageReplacementStrategy` (Interface implemented by `LRUReplacementStrategy`, `ClockReplacementStrategy`, `FIFOReplacementStrategy`), `ReplacementContext`
* **Method**: `selectVictim()`, `setStrategy()`, `evict()`

```mermaid
sequenceDiagram
    autonumber
    participant BPM as BufferPoolManager
    participant Context as ReplacementContext
    participant Strategy as PageReplacementStrategy (LRU)
    participant Frame as BufferFrame

    BPM->>+Context: evict()
    Context->>+Strategy: selectVictim()
    Strategy-->>-Context: victimFrameId
    Context->>+Frame: pinCountCheck(victimFrameId)
    Frame-->>-Context: Frame Unpinned (Ready to evict)
    Context-->>-BPM: victimFrameId
```

---

### 3.2. State Pattern
* **Pattern**: State Pattern
* **Class/Interface Applied**: `BufferFrame`, `BufferFrameState` (Enum: `CLEAN`, `DIRTY`, `PINNED`, `UNPINNED`)
* **Method**: `pin()`, `unpin()`, `markDirty()`, `setState()`

```mermaid
sequenceDiagram
    autonumber
    participant BPM as BufferPoolManager
    participant Frame as BufferFrame (State)

    BPM->>+Frame: pin()
    Frame->>+Frame: setState(PINNED)
    Frame-->>-BPM: Frame Pinned (pinCount=1)

    BPM->>+Frame: markDirty()
    Frame->>+Frame: setState(DIRTY)
    Frame-->>-BPM: Frame Dirty Marked

    BPM->>+Frame: unpin()
    Frame->>+Frame: setState(UNPINNED)
    Frame-->>-BPM: Frame Unpinned (pinCount=0)
```

---

### 3.3. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface Applied**: `Page` (Abstract Class), `DataPage`
* **Method**: `read()`, `deserialize()`, `modify()`, `serialize()`, `write()`

```mermaid
sequenceDiagram
    autonumber
    participant DFM as DiskFileManager
    participant Template as Page (Abstract Template)
    participant Concrete as DataPage (Concrete Class)

    DFM->>+Template: read(pageBytes)
    Template->>+Concrete: deserialize(pageBytes)
    Concrete-->>-Template: Deserialized Page OK
    Template->>+Concrete: modify(recordData)
    Concrete-->>-Template: Modified Page OK
    Template->>+Concrete: serialize()
    Concrete-->>-Template: byte[] pageData
    Template-->>-DFM: byte[] pageData ready for Disk
```

---

### 3.4. Iterator Pattern
* **Pattern**: Iterator Pattern
* **Class/Interface Applied**: `PageIterator`, `BTreeIterator`
* **Method**: `hasNext()`, `next()`

```mermaid
sequenceDiagram
    autonumber
    actor Exec as ExecutionEngine
    participant Iter as PageIterator / BTreeIterator
    participant Page as DataPage / LeafNode

    Exec->>+Iter: hasNext()
    Iter->>+Page: checkSlot(currentSlotIndex)
    Page-->>-Iter: true (Slot exists)
    Iter-->>-Exec: true

    Exec->>+Iter: next()
    Iter->>+Page: getRecord(currentSlotIndex++)
    Page-->>-Iter: Record data
    Iter-->>-Exec: Record data
```

---

# PHẦN II: SƠ ĐỒ TRÌNH TỰ THEO FEATURE CỐT LÕI

## 1. Fetch Page from Storage Engine (Buffer Pool Hit / Miss)
* **Feature**: Fetch Page (Read I/O Workflow)
* **Actor**: ExecutionEngine
* **Design Patterns**: Facade, Singleton, Adapter, State

```mermaid
sequenceDiagram
    autonumber
    actor Exec as ExecutionEngine
    participant SE as StorageEngine (Facade)
    participant BPM as BufferPoolManager (Singleton)
    participant Table as PageTable
    participant Frame as BufferFrame (State)
    participant DFM as DiskFileManager
    participant Adapter as StorageAdapter (LocalDiskAdapter)

    Exec->>+SE: fetchPage(101)
    SE->>+BPM: fetchPage(101)
    BPM->>+Table: lookup(101)
    alt Page 101 found in PageTable (Buffer Pool Hit)
        Table-->>BPM: frameId = 5
        BPM->>+Frame: pin()
        Frame-->>-BPM: Pinned
        BPM-->>SE: BufferFrame 5
    else Page 101 NOT found (Buffer Pool Miss)
        Table-->>-BPM: null
        BPM->>+BPM: evictPage() (if buffer full)
        BPM->>+DFM: readPage(101)
        DFM->>+Adapter: read(101)
        Adapter-->>-DFM: Raw Bytes 101
        DFM-->>-BPM: Page 101 Instance
        BPM->>+Table: insert(101, frameId)
        Table-->>-BPM: Insert OK
        BPM->>+Frame: pin()
        Frame-->>-BPM: Pinned
        BPM-->>-SE: BufferFrame 5
    end
    SE-->>-Exec: BufferFrame 5
```

---

## 2. Create & Build New Slotted Data Page
* **Feature**: Create Slotted Data Page
* **Actor**: StorageEngine / PageFactory
* **Design Patterns**: Factory Method, Builder, Template Method

```mermaid
sequenceDiagram
    autonumber
    actor Engine as StorageEngine
    participant Factory as PageFactory (Factory Method)
    participant Builder as PageBuilder (Builder)
    participant Page as DataPage (Template Method)

    Engine->>+Factory: createPage("DATA_PAGE")
    Factory->>+Builder: buildHeader(pageId = 102)
    Builder-->>Factory: Header OK
    Factory->>+Builder: buildSlots(slotCount = 0)
    Builder-->>Factory: Slots OK
    Factory->>+Builder: buildRecords(emptyRecords)
    Builder-->>Factory: Records OK
    Factory->>+Builder: build()
    Builder->>+Page: instantiate DataPage()
    Page-->>-Builder: DataPage instance
    Builder-->>-Factory: DataPage instance
    Factory-->>-Engine: DataPage instance
```

---

## 3. Evict Buffer Page using Strategy Pattern
* **Feature**: Buffer Page Eviction & Dirty Page Flush
* **Actor**: BufferPoolManager
* **Design Patterns**: Singleton, Strategy, State, Adapter

```mermaid
sequenceDiagram
    autonumber
    participant BPM as BufferPoolManager (Singleton)
    participant Context as ReplacementContext
    participant Strategy as PageReplacementStrategy (LRU)
    participant Frame as BufferFrame (State)
    participant Flush as FlushManager
    participant DFM as DiskFileManager
    participant Adapter as StorageAdapter

    BPM->>+Context: evict()
    Context->>+Strategy: selectVictim()
    Strategy-->>-Context: frameId = 3
    Context->>+Frame: checkState()
    Frame-->>-Context: State = DIRTY, Unpinned
    Context-->>-BPM: frameId = 3
    alt Frame State is DIRTY
        BPM->>+Flush: flushDirtyPages()
        Flush->>+DFM: writePage(page3)
        DFM->>+Adapter: write(page3)
        Adapter-->>-DFM: Write Success
        DFM-->>-Flush: Page 3 Flushed
        Flush-->>-BPM: Flush Complete
        BPM->>+Frame: setState(CLEAN)
        Frame-->>-BPM: State Clean
    end
    BPM->>+Frame: releaseFrame()
    Frame-->>-BPM: Frame 3 Freed
```

---

## 4. Search Key in B+Tree Index (Composite & Iterator)
* **Feature**: B+Tree Index Range Scan
* **Actor**: ExecutionEngine
* **Design Patterns**: Composite, Iterator

```mermaid
sequenceDiagram
    autonumber
    actor Exec as ExecutionEngine
    participant Cursor as BTreeCursor
    participant Root as BTreeNode (InternalNode)
    participant Leaf as LeafNode
    participant Iter as BTreeIterator (Iterator)

    Exec->>+Cursor: seek(searchKey = 50)
    Cursor->>+Root: search(50)
    Root->>+Leaf: search(50)
    Leaf-->>-Root: LeafNode instance
    Root-->>-Cursor: LeafNode instance
    Cursor-->>-Exec: Cursor positioned at LeafNode

    Exec->>+Iter: hasNext()
    Iter-->>Exec: true
    Exec->>+Iter: next()
    Iter->>+Leaf: getEntry(slotIndex++)
    Leaf-->>-Iter: IndexEntry (Key: 50, RID: Page 10, Slot 2)
    Iter-->>-Exec: IndexEntry Data
```

---

## 5. Write Page via Storage Adapter (Local Disk / Memory / Cloud)
* **Feature**: Write Page I/O via Storage Adapter
* **Actor**: StorageEngine / FlushManager
* **Design Patterns**: Facade, Adapter, Template Method

```mermaid
sequenceDiagram
    autonumber
    actor Flush as FlushManager / DiskFileManager
    participant Adapter as StorageAdapter (CloudStorageAdapter / LocalDiskAdapter)
    participant Media as Target Media (Disk / S3 Cloud)

    Flush->>+Adapter: write(pageId = 105, pageData)
    Adapter->>+Adapter: serializePage(pageData)
    Adapter->>+Media: putObject / writeBlock(page105Bytes)
    Media-->>-Adapter: Success Ack
    Adapter-->>-Flush: Write Operation Successful
```
