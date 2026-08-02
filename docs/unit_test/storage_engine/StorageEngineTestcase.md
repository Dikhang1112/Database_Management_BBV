# Storage Engine Subsystem Unit Test Scenarios

This document defines all unit test scenarios for `Storage Engine` subsystem (`StorageEngine`, `BufferPoolManager`, `PageReplacementStrategy`, `PageFactory`, `Page`, `BufferFrame`, `PageBuilder`, `PageIterator`, `BTreeNode`, `DiskFileManager`, and helper classes), covering both positive (happy path) and negative (edge cases and exceptions) scenarios as specified in [MindmapTest.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/storage_engine/MindmapTest.md) and [SequeceTestCase.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/storage_engine/SequeceTestCase.md).

Each test scenario follows this standard format:
- **Test method:** JUnit method name.
- **Sequence diagram:** Corresponding sequence diagram ID in `SequeceTestCase.md`.
- **Input:** Input objects or parameters.
- **Why:** Rationale and design pattern requirement for verifying this test case.
- **Expected output:** Assertions or expected exceptions.

---

## 1. StorageEngineTest (Facade Pattern)

### TC-01. Fetch Page Facade (Happy Path)
- **Test method:** `fetchPage_ShouldReturnBufferFrame_WhenPageExists`
- **Sequence diagram:** `TC-01`
- **Input:** Page ID: `1001L`
- **Why:** Verifies that `StorageEngine` acts as a unified Facade entry point, delegating page fetching requests directly to `BufferPoolManager`.
- **Expected output:**
  - Returns target `BufferFrame` instance containing `pageId = 1001L`.

### TC-01A. Allocate Page Facade (Happy Path)
- **Test method:** `allocatePage_ShouldDelegateToPageFactoryAndBufferPoolManager_WhenPageTypeProvided`
- **Sequence diagram:** `TC-01A`
- **Input:** `PageType.DATA`
- **Why:** Ensures Facade coordinates `PageFactory` creation and `BufferPoolManager` page registration seamlessly.
- **Expected output:**
  - Returns newly instantiated `DataPage` registered in buffer pool.

### TC-01B. Free Page Facade (Happy Path)
- **Test method:** `freePage_ShouldRemovePageFromBufferPoolManager_WhenValidPageId`
- **Sequence diagram:** `TC-01B`
- **Input:** Page ID: `1001L`
- **Why:** Ensures Facade allows freeing allocated pages from buffer management.
- **Expected output:**
  - `BufferPoolManager` evicts/frees page `1001L` without exception.

### TC-01C. Flush All Facade (Happy Path)
- **Test method:** `flushAll_ShouldInvokeFlushOnBufferPoolAndDiskManager_WhenTriggered`
- **Sequence diagram:** `TC-01C`
- **Input:** None
- **Why:** Verifies Facade provides global flush operation across all subsystem components.
- **Expected output:**
  - All dirty pages in buffer pool written to disk storage.

### TC-01D. Fetch Page Facade - Invalid Page ID
- **Test method:** `fetchPage_ShouldThrowException_WhenPageIdIsNegative`
- **Sequence diagram:** `TC-01D`
- **Input:** Page ID: `-1L`
- **Why:** Prevents invalid negative page identifiers from reaching buffer pool layer.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Page ID must be non-negative").

### TC-01E. Allocate Page Facade - Null Page Type
- **Test method:** `allocatePage_ShouldThrowException_WhenPageTypeIsNull`
- **Sequence diagram:** `TC-01E`
- **Input:** `PageType` = `null`
- **Why:** Ensures Facade validates page type inputs before Factory invocation.
- **Expected output:**
  - Throws `IllegalArgumentException` ("PageType cannot be null").

---

## 2. BufferPoolManagerTest (Singleton Pattern)

### TC-02. Get Instance (Singleton Verification)
- **Test method:** `getInstance_ShouldReturnSameSingletonInstance_Always`
- **Sequence diagram:** `TC-02`
- **Input:** None
- **Why:** Guarantees Singleton pattern enforcement for central buffer pool management.
- **Expected output:**
  - `BufferPoolManager.getInstance()` returns identical reference on repeated calls.

### TC-02A. Fetch Page - Cache Miss (Read Disk)
- **Test method:** `fetchPage_ShouldReadFromDiskAndBufferFrame_WhenPageNotInPool`
- **Sequence diagram:** `TC-02A`
- **Input:** Page ID: `2002L` (not present in `pageTable`)
- **Why:** Verifies buffer manager loads page from `DiskFileManager` on cache miss and pins buffer frame.
- **Expected output:**
  - `DiskFileManager.readPage(2002L)` is called once.
  - Page inserted into `pageTable`.
  - Frame pinCount = 1.

### TC-02B. Fetch Page - Cache Hit (Return Memory)
- **Test method:** `fetchPage_ShouldReturnBufferedFrameDirectly_WhenPageInPool`
- **Sequence diagram:** `TC-02B`
- **Input:** Page ID: `2002L` (already buffered)
- **Why:** Ensures cached frames are returned instantly without disk I/O.
- **Expected output:**
  - `DiskFileManager.readPage()` is **not** called.
  - Returns existing `BufferFrame`.

### TC-02C. Pin & Unpin Page Lifetime
- **Test method:** `pinAndUnpin_ShouldManageFramePinCountCorrectly`
- **Sequence diagram:** `TC-02C`
- **Input:** Page ID: `2002L`
- **Why:** Verifies correct increment and decrement of frame pin count during transaction lifetime.
- **Expected output:**
  - `pinPage()` increments `pinCount` to 2.
  - `unpinPage()` decrements `pinCount` back to 1.

### TC-02D. Evict Dirty Victim Page
- **Test method:** `evictPage_ShouldFlushDirtyVictimPageToDisk_WhenBufferPoolIsFull`
- **Sequence diagram:** `TC-02D`
- **Input:** Buffer pool at max capacity, new fetch request for Page `3000L`
- **Why:** Verifies buffer manager uses replacement strategy to evict victim and flushes dirty pages before reuse.
- **Expected output:**
  - Victim frame selected by strategy.
  - `DiskFileManager.writePage(victimPage)` called.
  - Victim evicted and replaced by new frame.

### TC-02E. Evict Fail - All Pages Pinned
- **Test method:** `evictPage_ShouldThrowException_WhenAllFramesInPoolArePinned`
- **Sequence diagram:** `TC-02E`
- **Input:** Buffer pool full, all frame `pinCount > 0`
- **Why:** Prevents overwriting actively pinned pages in memory.
- **Expected output:**
  - Throws `BufferPoolFullException` ("All frames in buffer pool are pinned").

### TC-02F. Flush Dirty Page To Disk
- **Test method:** `flushPage_ShouldWriteDirtyPageToDiskAndResetDirtyFlag`
- **Sequence diagram:** `TC-02F`
- **Input:** Page ID: `2002L` (state = DIRTY)
- **Why:** Ensures targeted flush persists dirty frame state to storage adapter.
- **Expected output:**
  - `DiskFileManager.writePage()` executed.
  - Buffer frame state transitioned to `CLEAN`.

### TC-02G. Clear Buffer Pool
- **Test method:** `clearPool_ShouldReleaseAllFramesAndResetPageTable`
- **Sequence diagram:** `TC-02G`
- **Input:** BufferPool containing multiple clean and dirty frames
- **Why:** Verifies complete pool shutdown and memory release during database unmount/stop.
- **Expected output:**
  - Dirty pages flushed to disk.
  - `pageTable` emptied and frame indices returned to `FreeFrameList`.

---

## 3. PageReplacementStrategyTest (Strategy Pattern)

### TC-03. LRU Replacement Strategy
- **Test method:** `selectVictim_LRUStrategy_ShouldEvictLeastRecentlyUsedFrame`
- **Sequence diagram:** `TC-03`
- **Input:** Access sequence: Frame 1, Frame 2, Frame 3, Frame 1, Frame 2
- **Why:** Verifies LRU strategy correctly selects frame 3 (least recently accessed).
- **Expected output:**
  - `selectVictim()` returns frame index 3.

### TC-03A. Clock Replacement Strategy
- **Test method:** `selectVictim_ClockStrategy_ShouldEvictFrameWithZeroRefBit`
- **Sequence diagram:** `TC-03A`
- **Input:** Frame refBits: [1, 1, 0, 1]
- **Why:** Verifies Clock algorithm second-chance mechanism skips refBit=1 and selects first refBit=0 frame.
- **Expected output:**
  - `selectVictim()` returns frame index 2.

### TC-03B. FIFO Replacement Strategy
- **Test method:** `selectVictim_FIFOStrategy_ShouldEvictOldestLoadedFrame`
- **Sequence diagram:** `TC-03B`
- **Input:** Insertion sequence: Frame 0 (t=0), Frame 1 (t=5), Frame 2 (t=10)
- **Why:** Verifies FIFO strategy evicts the frame loaded earliest.
- **Expected output:**
  - `selectVictim()` returns frame index 0.

### TC-03C. Skip Pinned Victim Frame
- **Test method:** `selectVictim_ShouldSkipPinnedFrames_WhenSelectingVictim`
- **Sequence diagram:** `TC-03C`
- **Input:** Candidate victim frame is PINNED (`pinCount = 1`)
- **Why:** Ensures strategy implementations skip pinned frames regardless of policy rules.
- **Expected output:**
  - Strategy advances to next unpinned candidate frame.

### TC-03D. Replacement Strategy Victim Not Found
- **Test method:** `selectVictim_ShouldReturnNegativeOne_WhenNoUnpinnedFramesAvailable`
- **Sequence diagram:** `TC-03D`
- **Input:** All frames PINNED
- **Why:** Ensures strategy returns error indicator (-1) safely without throwing unhandled internal exceptions.
- **Expected output:**
  - Returns `-1`.

### TC-03E. Dynamic Strategy Switching
- **Test method:** `setStrategy_ShouldDynamicallySwitchReplacementPolicyAtRuntime`
- **Sequence diagram:** `TC-03E`
- **Input:** Switch context from `LRUReplacementStrategy` to `ClockReplacementStrategy`
- **Why:** Verifies Strategy pattern flexibility allowing dynamic replacement algorithm swapping.
- **Expected output:**
  - `ReplacementContext` uses new strategy policy on subsequent `evict()` calls.

---

## 4. PageFactoryTest (Factory Method Pattern)

### TC-04. Create DataPage (Happy Path)
- **Test method:** `createPage_ShouldInstantiateDataPage_WhenPageTypeIsData`
- **Sequence diagram:** `TC-04`
- **Input:** `PageType.DATA`
- **Why:** Verifies Factory Method instantiates concrete `DataPage` implementation.
- **Expected output:**
  - Returns object of type `DataPage`.

### TC-04A. Create IndexPage (Happy Path)
- **Test method:** `createPage_ShouldInstantiateIndexPage_WhenPageTypeIsIndex`
- **Sequence diagram:** `TC-04A`
- **Input:** `PageType.INDEX`
- **Why:** Verifies Factory Method instantiates concrete `IndexPage` implementation.
- **Expected output:**
  - Returns object of type `IndexPage`.

### TC-04B. Create CatalogPage (Happy Path)
- **Test method:** `createPage_ShouldInstantiateCatalogPage_WhenPageTypeIsCatalog`
- **Sequence diagram:** `TC-04B`
- **Input:** `PageType.CATALOG`
- **Why:** Verifies Factory Method instantiates concrete `CatalogPage` implementation.
- **Expected output:**
  - Returns object of type `CatalogPage`.

### TC-04C. Create Page - Unknown Type
- **Test method:** `createPage_ShouldThrowException_WhenPageTypeIsUnknownOrInvalid`
- **Sequence diagram:** `TC-04C`
- **Input:** Invalid enum value or custom unknown type
- **Why:** Prevents factory from returning uninitialized or null page structures.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Unsupported page type").

---

## 5. PageTemplateTest (Template Method Pattern)

### TC-05. Process Page I/O - DataPage (Happy Path)
- **Test method:** `processPageIO_DataPage_ShouldExecuteStandardReadDeserializeSequence`
- **Sequence diagram:** `TC-05`
- **Input:** Raw byte array `rawBytes[]` for DataPage
- **Why:** Verifies Template Method pattern skeleton (`read()` -> `deserialize()` -> `modify()` -> `serialize()` -> `write()`) on `DataPage`.
- **Expected output:**
  - `DataPage.deserialize()` invoked.
  - Page header and slots populated cleanly.

### TC-05A. Process Page I/O - IndexPage
- **Test method:** `processPageIO_IndexPage_ShouldExecuteIndexSerializationSequence`
- **Sequence diagram:** `TC-05A`
- **Input:** Raw byte array `rawBytes[]` for IndexPage
- **Why:** Verifies Template Method subclass override for `IndexPage` key-pointer serialization.
- **Expected output:**
  - `IndexPage.deserialize()` executed.

### TC-05B. Process Page I/O - CatalogPage
- **Test method:** `processPageIO_CatalogPage_ShouldSerializeMetadataEntries`
- **Sequence diagram:** `TC-05B`
- **Input:** Raw byte array `rawBytes[]` for CatalogPage
- **Why:** Verifies Template Method subclass override for `CatalogPage` table metadata serialization.
- **Expected output:**
  - `CatalogPage.serialize()` returns valid byte representation of catalog records.

### TC-05C. Insert Record Update Header
- **Test method:** `insertRecord_ShouldUpdateHeaderAndSlotDirectory_WhenSpaceAvailable`
- **Sequence diagram:** `TC-05C`
- **Input:** `Record` instance (payload = `"column_val"`)
- **Why:** Ensures record insertion updates `SlotDirectory` offsets and `PageHeader` slot count correctly.
- **Expected output:**
  - `insertRecord()` returns `true`.
  - `PageHeader.getSlotCount()` incremented by 1.

### TC-05D. Delete Record Slot Free
- **Test method:** `deleteRecord_ShouldMarkSlotAsFree_WhenValidRIDProvided`
- **Sequence diagram:** `TC-05D`
- **Input:** `RID(pageId=10, slotNo=2)`
- **Why:** Ensures deleting record frees slot entry in `SlotDirectory` for future reuse.
- **Expected output:**
  - `deleteRecord()` returns `true`.
  - Slot offset marked as tombstone (-1).

### TC-05E. Insert Record Overflow Exception
- **Test method:** `insertRecord_ShouldReturnFalse_WhenPageFreeSpaceIsExceeded`
- **Sequence diagram:** `TC-05E`
- **Input:** Large `Record` exceeding remaining free page byte capacity
- **Why:** Prevents page corruption due to writing beyond page boundary size limit (4KB/8KB).
- **Expected output:**
  - `insertRecord()` returns `false`.

### TC-05F. Update Record In Place
- **Test method:** `updateRecord_ShouldModifyDataInPlace_WhenSlotNoIsValid`
- **Sequence diagram:** `TC-05F`
- **Input:** `RID(pageId=10, slotNo=1)`, new updated `Record` object
- **Why:** Verifies in-place updating of existing page record payload without changing slot offset.
- **Expected output:**
  - `updateRecord()` returns `true`.
  - Record payload updated at target slot directory offset.

### TC-05G. Compact Page Defragmentation
- **Test method:** `compactPage_ShouldDefragmentFreeSpace_WhenMultipleRecordsDeleted`
- **Sequence diagram:** `TC-05G`
- **Input:** `DataPage` with fragmented empty gaps between active records
- **Why:** Ensures page defragmentation re-aligns active record bytes into contiguous space.
- **Expected output:**
  - All active record bytes shifted to top of page payload area.
  - Contiguous free space chunk maximized at bottom.

---

## 6. BufferFrameStateTest (State Pattern)

### TC-06. Pin Frame State Transition (Happy Path)
- **Test method:** `pin_ShouldTransitionToPinnedStateAndIncrementPinCount`
- **Sequence diagram:** `TC-06`
- **Input:** BufferFrame in `UNPINNED` state
- **Why:** Verifies State pattern transition when frame is pinned by transaction worker.
- **Expected output:**
  - `getState()` returns `BufferFrameState.PINNED`.
  - `getPinCount()` returns 1.

### TC-06A. Unpin Frame State Transition
- **Test method:** `unpin_ShouldDecrementPinCountAndTransitionToUnpinned_WhenCountReachesZero`
- **Sequence diagram:** `TC-06A`
- **Input:** BufferFrame in `PINNED` state (`pinCount = 1`)
- **Why:** Verifies state transition back to `UNPINNED` when last pin is released.
- **Expected output:**
  - `getPinCount()` returns 0.
  - `getState()` returns `BufferFrameState.UNPINNED`.

### TC-06B. Mark Dirty Frame State
- **Test method:** `markDirty_ShouldTransitionStateToDirty`
- **Sequence diagram:** `TC-06B`
- **Input:** BufferFrame modified in memory
- **Why:** Ensures frame state correctly captures dirty status for flush handling.
- **Expected output:**
  - `getState()` returns `BufferFrameState.DIRTY`.

### TC-06C. BufferFrame State Mutator
- **Test method:** `setState_ShouldExplicitlyUpdateFrameStateEnum`
- **Sequence diagram:** `TC-06C`
- **Input:** `BufferFrameState.CLEAN`
- **Why:** Verifies explicit state encapsulation in `BufferFrame`.
- **Expected output:**
  - `getState()` equals `BufferFrameState.CLEAN`.

### TC-06D. Unpin Frame Already Zero Exception
- **Test method:** `unpin_ShouldThrowException_WhenPinCountIsAlreadyZero`
- **Sequence diagram:** `TC-06D`
- **Input:** BufferFrame with `pinCount = 0`
- **Why:** Prevents underflow of pinCount counter due to unmatched unpin calls.
- **Expected output:**
  - Throws `IllegalStateException` ("Pin count cannot be negative").

---

## 7. PageBuilderTest (Builder Pattern)

### TC-07. Build Complete Page (Happy Path)
- **Test method:** `build_ShouldConstructPageWithHeaderSlotsAndRecords_WhenFluentMethodsInvoked`
- **Sequence diagram:** `TC-07`
- **Input:** Fluent builder calls: `setPageType()`, `buildHeader()`, `buildSlots()`, `addRecord()`
- **Why:** Verifies Builder pattern constructs complex `Page` step-by-step fluently.
- **Expected output:**
  - Returns fully constructed `Page` instance.

### TC-07A. Build Page Header
- **Test method:** `buildHeader_ShouldInitializePageIdAndLSNInHeader`
- **Sequence diagram:** `TC-07A`
- **Input:** `pageId = 50`, `lsn = 1002`
- **Why:** Ensures Builder properly configures header metadata fields.
- **Expected output:**
  - `Page.getHeader().getPageId()` equals 50.
  - `Page.getHeader().getLsn()` equals 1002.

### TC-07B. Build Slot Directory
- **Test method:** `buildSlots_ShouldPreallocateSlotOffsets`
- **Sequence diagram:** `TC-07B`
- **Input:** `initialSlots = 16`
- **Why:** Verifies Builder pre-allocates slot directory capacity.
- **Expected output:**
  - `SlotDirectory` capacity equals 16.

### TC-07C. Add Record Builder
- **Test method:** `addRecord_ShouldAppendRecordToBuilderList`
- **Sequence diagram:** `TC-07C`
- **Input:** `Record` mock object
- **Why:** Ensures Builder accumulates records prior to final build invocation.
- **Expected output:**
  - Constructed Page contains added `Record`.

### TC-07D. Build Missing PageType Exception
- **Test method:** `build_ShouldThrowException_WhenPageTypeIsNotSet`
- **Sequence diagram:** `TC-07D`
- **Input:** Builder without calling `setPageType()`
- **Why:** Ensures Builder enforces mandatory fields before instantiation.
- **Expected output:**
  - Throws `IllegalStateException` ("PageType must be set before building page").

---

## 8. PageIteratorTest (Iterator Pattern)

### TC-08. Page Iterator Traversal (Happy Path)
- **Test method:** `iterator_ShouldTraverseAllRecordsSequentially_WhenRecordsExist`
- **Sequence diagram:** `TC-08`
- **Input:** `Page` containing 3 records
- **Why:** Verifies Iterator pattern implementation for traversing page records without exposing internal structure.
- **Expected output:**
  - `hasNext()` returns `true` 3 times, then `false`.
  - `next()` yields each `Record` in sequence.

### TC-08A. Iterator HasNext Boundary
- **Test method:** `hasNext_ShouldReturnFalse_WhenPageIsEmptyOrFullyTraversed`
- **Sequence diagram:** `TC-08A`
- **Input:** Empty `Page` with 0 records
- **Why:** Verifies boundary condition check for empty collections.
- **Expected output:**
  - `hasNext()` returns `false` immediately.

### TC-08B. Iterator Next NoSuchElement Exception
- **Test method:** `next_ShouldThrowException_WhenNoMoreRecordsToTraverse`
- **Sequence diagram:** `TC-08B`
- **Input:** Call `next()` when `hasNext()` is `false`
- **Why:** Standard Java `Iterator` contract enforcement.
- **Expected output:**
  - Throws `NoSuchElementException` ("No more records in PageIterator").

### TC-08C. Iterator Skip Tombstone Slot
- **Test method:** `iterator_ShouldSkipDeletedTombstoneSlots_DuringTraversal`
- **Sequence diagram:** `TC-08C`
- **Input:** `Page` with records at slots [0 (valid), 1 (deleted tombstone), 2 (valid)]
- **Why:** Ensures Iterator automatically bypasses deleted slots in slot directory.
- **Expected output:**
  - Traverses records at slot 0 and slot 2, skipping slot 1.

---

## 9. BTreeIndexTest (Composite & Iterator Pattern)

### TC-09. LeafNode Insert & Search (Happy Path)
- **Test method:** `search_ShouldReturnPayload_WhenKeyExistsInLeafNode`
- **Sequence diagram:** `TC-09`
- **Input:** Insert `key = 42, value = RID(1, 5)`, Search `key = 42`
- **Why:** Verifies Composite pattern leaf component data storage and lookup.
- **Expected output:**
  - Returns `RID(1, 5)`.

### TC-09A. InternalNode Route Child
- **Test method:** `findChild_ShouldRouteToCorrectChildNode_BasedOnKeyRange`
- **Sequence diagram:** `TC-09A`
- **Input:** `InternalNode` with keys [10, 20, 30], search `key = 25`
- **Why:** Verifies Composite pattern internal node child routing logic.
- **Expected output:**
  - Routes to child node responsible for key range `[20, 30)`.

### TC-09B. LeafNode Split On Overflow
- **Test method:** `split_LeafNode_ShouldDivideDataEntriesEqually_WhenCapacityExceeded`
- **Sequence diagram:** `TC-09B`
- **Input:** Insert key into full `LeafNode` (capacity = 4)
- **Why:** Verifies B+Tree leaf node splitting algorithm.
- **Expected output:**
  - Returns new sibling `LeafNode`.
  - Entries split 2-3 between original and sibling node.

### TC-09C. InternalNode Split Promote Key
- **Test method:** `split_InternalNode_ShouldPromoteMiddleKeyToParent_WhenFull`
- **Sequence diagram:** `TC-09C`
- **Input:** Insert child pointer into full `InternalNode`
- **Why:** Verifies B+Tree internal node splitting and key promotion up the tree structure.
- **Expected output:**
  - Middle key promoted to parent node.
  - New sibling `InternalNode` created.

### TC-09D. BTreeCursor Seek Leaf
- **Test method:** `seek_ShouldTraverseRootToLeafNode_ForGivenKey`
- **Sequence diagram:** `TC-09D`
- **Input:** `key = 75`
- **Why:** Verifies `BTreeCursor` root-to-leaf traversal algorithm.
- **Expected output:**
  - Returns target `LeafNode` containing key range for 75.

### TC-09E. BTreeIterator Sorted Traversal
- **Test method:** `iterator_BTreeIterator_ShouldReturnEntriesInAscendingSortedOrder`
- **Sequence diagram:** `TC-09E`
- **Input:** B+Tree containing unsorted insertion order [50, 10, 30, 20]
- **Why:** Verifies `BTreeIterator` sequential leaf-level linked list traversal.
- **Expected output:**
  - Iterates keys in exact order: 10, 20, 30, 50.

### TC-09F. BTree Search Key Not Found
- **Test method:** `search_ShouldReturnNull_WhenKeyDoesNotExistInTree`
- **Sequence diagram:** `TC-09F`
- **Input:** Search `key = 999` (non-existent)
- **Why:** Ensures B+Tree returns `null` safely when key is not present.
- **Expected output:**
  - Returns `null`.

### TC-09G. BTreeNode Delete Underflow Rebalance
- **Test method:** `delete_BTreeNode_ShouldMergeOrBorrow_WhenNodeUnderflows`
- **Sequence diagram:** `TC-09G`
- **Input:** Delete key from `LeafNode` bringing entry count below minimum threshold (e.g. `< capacity/2`)
- **Why:** Verifies B+Tree node deletion, key borrowing from sibling, and node merging algorithm.
- **Expected output:**
  - Node underflow resolved by borrowing key from adjacent sibling or merging with sibling node.

---

## 10. DiskFileManagerTest (Adapter Pattern)

### TC-10. LocalDiskAdapter Read Page (Happy Path)
- **Test method:** `readPage_LocalDiskAdapter_ShouldReadRawBytesFromLocalFileSystem`
- **Sequence diagram:** `TC-10`
- **Input:** `pageId = 100L`, `StorageAdapter` = `LocalDiskAdapter`
- **Why:** Verifies Adapter pattern delegation to local physical disk I/O implementation.
- **Expected output:**
  - `LocalDiskAdapter.read(100L)` returns 4096-byte raw array.

### TC-10A. MemoryStorageAdapter Write Page
- **Test method:** `writePage_MemoryStorageAdapter_ShouldWriteBytesToInMemoryMap`
- **Sequence diagram:** `TC-10A`
- **Input:** `Page` instance, `StorageAdapter` = `MemoryStorageAdapter`
- **Why:** Verifies Adapter pattern implementation for fast in-memory mock/testing storage.
- **Expected output:**
  - Bytes stored in `MemoryStorageAdapter` internal map under key `100L`.

### TC-10B. CloudStorageAdapter Upload Page
- **Test method:** `writePage_CloudStorageAdapter_ShouldUploadPageToCloudObjectStorage`
- **Sequence diagram:** `TC-10B`
- **Input:** `Page` instance, `StorageAdapter` = `CloudStorageAdapter`
- **Why:** Verifies Adapter pattern seamless extension to cloud blob/object storage.
- **Expected output:**
  - `CloudStorageAdapter.write(page)` executes cloud API payload upload successfully.

### TC-10C. StorageAdapter Dynamic Switch
- **Test method:** `setStorageAdapter_ShouldSwitchActiveAdapterAtRuntime`
- **Sequence diagram:** `TC-10C`
- **Input:** Switch `DiskFileManager` adapter from `LocalDiskAdapter` to `MemoryStorageAdapter`
- **Why:** Ensures `DiskFileManager` decoupled architecture allows runtime storage backend switching.
- **Expected output:**
  - Subsequent `readPage()` calls route through newly set `MemoryStorageAdapter`.

### TC-10D. PageSerializer & Deserializer Integrity
- **Test method:** `serializeAndDeserialize_ShouldMaintainDataIntegrity`
- **Sequence diagram:** `TC-10D`
- **Input:** `Page` instance with headers, slots, and 5 records
- **Why:** Verifies bi-directional serialization and deserialization exact parity.
- **Expected output:**
  - Deserialized `Page` properties match original `Page` fields identically.

### TC-10E. ReadPage Page Not Found Exception
- **Test method:** `readPage_ShouldThrowException_WhenPageIdDoesNotExistOnDisk`
- **Sequence diagram:** `TC-10E`
- **Input:** `pageId = 999999L` (non-existent file block)
- **Why:** Ensures storage adapter handles missing disk blocks gracefully.
- **Expected output:**
  - Throws `PageNotFoundException` ("Page ID 999999 not found on storage device").

### TC-10F. DiskFileManager Flush Adapter Sync
- **Test method:** `flush_DiskFileManager_ShouldSyncStorageAdapterState`
- **Sequence diagram:** `TC-10F`
- **Input:** `DiskFileManager` with pending writes
- **Why:** Verifies flush operation invokes underlying adapter physical sync/flush method.
- **Expected output:**
  - `StorageAdapter.flush()` invoked.
