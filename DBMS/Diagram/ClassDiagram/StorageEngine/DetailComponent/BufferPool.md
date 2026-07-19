```mermaid
classDiagram
    direction TB

    class BufferPoolManager {
        -Frame[] poolFrames
        -Map~Integer, Integer~ pageFrameMap
        -PageReplacer replacer
        -DiskFileManager fileManager
        -Object poolLatch
        +fetchPage(int pageID) DataPage
        +unpinPage(int pageID, boolean isDirty) boolean
        +flushPage(int pageID) boolean
        +newPage() DataPage
        +deletePage(int pageID) boolean
    }

    class Frame {
        -int frameID
        -int pageID
        -int pinCount
        -boolean isDirty
        -DataPage pageReference
        -Object frameLatch
        +pin() void
        +unpin() void
        +isEvictable() boolean
    }

    class PageReplacer {
        <<interface>>
        +recordAccess(int frameID) void
        +pin(int frameID) void
        +unpin(int frameID) void
        +selectVictim() int
    }

    class ClockReplacer {
        -boolean[] referenceBits
        -int clockHand
        -int totalFrames
        +selectVictim() int
    }

    %% Relationships
    BufferPoolManager "1" *-- "*" Frame : allocates fixed memory array
    BufferPoolManager "1" *-- "1" PageReplacer : delegates eviction strategy
    PageReplacer <|.. ClockReplacer : implements enterprise memory swap
```
