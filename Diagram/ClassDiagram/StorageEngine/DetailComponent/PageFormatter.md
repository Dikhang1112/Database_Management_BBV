classDiagram
    direction TB

    class DataPage {
        -int pageID
        -int tableID
        -byte[] binaryBuffer
        -SlottedPageFormatter formatter
        +insertRecord(byte[] recordBytes) boolean
        +getRecord(int slotNumber) byte[]
    }

    class SlottedPageFormatter {
        -byte[] buffer
        -int pageSize
        +writeHeader(int pageID, int slotCount) void
        +readPageID() int
        +readSlotCount() int
        +getSlotOffset(int slotNumber) int
        +writeSlot(int slotNumber, int recordOffset) void
        +hasSpaceFor(int recordLength) boolean
        +insertRecordAtEnd(byte[] recordBytes) int
        +readRecordFromOffset(int offset, int length) byte[]
        +getFreeSpacePointer() int
        +getFreeSpaceEnd() int
    }

    class SlottedPageHeader {
        +int pageID
        +int slotCount
        +int freeSpacePointerOffset
        +int freeSpaceEndOffset
    }
    
    class PageSlotDirectory {
        +int slotNumber
        +int recordOffset
        +int recordLength
    }

    %% Relationships
    DataPage "1" *-- "1" SlottedPageFormatter : delegates byte mutations to
    SlottedPageFormatter ..> SlottedPageHeader : maps binary start to
    SlottedPageFormatter ..> PageSlotDirectory : grows array downward from header