package level_3.storage_engine;
import java.nio.ByteBuffer;

public class SlottedPageFormatter {
    private final byte[] buffer;
    private final int pageSize;

    // Fixed Header size in bytes
    // 0-3: pageID, 4-7: slotCount
    // 8-11: freeSpacePointerOffset, 12-15: freeSpaceEndOffset
    private static final int HEADER_SIZE = 16;

    // Size of each slot directory entry in bytes
    private static final int SLOT_SIZE = 8;

    public SlottedPageFormatter(byte[] buffer) {
        this.buffer = buffer;
        this.pageSize = buffer.length;
    }

    /**
     * Initializes the page header for a new page.
     */
    public void writeHeader(int pageID, int slotCount) {
        ByteBuffer.wrap(buffer, 0, 4).putInt(pageID);
        ByteBuffer.wrap(buffer, 4, 4).putInt(slotCount);

        // Free space starts right after the fixed header
        int initialFreeSpacePointer = HEADER_SIZE;
        ByteBuffer.wrap(buffer, 8, 4).putInt(initialFreeSpacePointer);

        // Free space ends at the physical end of the page
        int initialFreeSpaceEnd = pageSize;
        ByteBuffer.wrap(buffer, 12, 4).putInt(initialFreeSpaceEnd);
    }

    public int readPageID() {
        return ByteBuffer.wrap(buffer, 0, 4).getInt();
    }

    public int readSlotCount() {
        return ByteBuffer.wrap(buffer, 4, 4).getInt();
    }

    public int getFreeSpacePointer() {
        return ByteBuffer.wrap(buffer, 8, 4).getInt();
    }

    private void setFreeSpacePointer(int offset) {
        ByteBuffer.wrap(buffer, 8, 4).putInt(offset);
    }

    public int getFreeSpaceEnd() {
        return ByteBuffer.wrap(buffer, 12, 4).getInt();
    }

    private void setFreeSpaceEnd(int offset) {
        ByteBuffer.wrap(buffer, 12, 4).putInt(offset);
    }

    /**
     * Gets the physical record offset from the slot directory.
     */
    public int getSlotOffset(int slotNumber) {
        int slotIndexInIndex = HEADER_SIZE + (slotNumber * SLOT_SIZE);
        return ByteBuffer.wrap(buffer, slotIndexInIndex, 4).getInt();
    }

    /**
     * Gets the length of the record at a specific slot.
     */
    public int getSlotLength(int slotNumber) {
        int slotIndexInIndex = HEADER_SIZE + (slotNumber * SLOT_SIZE) + 4;
        return ByteBuffer.wrap(buffer, slotIndexInIndex, 4).getInt();
    }

    /**
     * Writes record pointers to the slot directory.
     */
    public void writeSlot(int slotNumber, int recordOffset, int recordLength) {
        int slotStartIndex = HEADER_SIZE + (slotNumber * SLOT_SIZE);
        ByteBuffer.wrap(buffer, slotStartIndex, 4).putInt(recordOffset);
        ByteBuffer.wrap(buffer, slotStartIndex + 4, 4).putInt(recordLength);

        int currentSlots = readSlotCount();
        if (slotNumber >= currentSlots) {
            ByteBuffer.wrap(buffer, 4, 4).putInt(slotNumber + 1);
            setFreeSpacePointer(slotStartIndex + SLOT_SIZE);
        }
    }

    /**
     * Checks if the page has sufficient space for a new record.
     */
    public boolean hasSpaceFor(int recordLength) {
        int spaceRequired = recordLength + SLOT_SIZE;
        int availableSpace = getFreeSpaceEnd() - getFreeSpacePointer();
        return spaceRequired <= availableSpace;
    }

    /**
     * Inserts raw data into the data payload area from the end of the page.
     */
    public int insertRecordAtEnd(byte[] recordBytes) {
        if (!hasSpaceFor(recordBytes.length)) {
            return -1; // Overflow error code
        }

        int newFreeSpaceEnd = getFreeSpaceEnd() - recordBytes.length;
        System.arraycopy(recordBytes, 0, buffer, newFreeSpaceEnd, recordBytes.length);
        setFreeSpaceEnd(newFreeSpaceEnd);

        return newFreeSpaceEnd;
    }

    /**
     * Reads raw bytes from a specific offset.
     */
    public byte[] readRecordFromOffset(int offset, int length) {
        byte[] record = new byte[length];
        System.arraycopy(buffer, offset, record, 0, length);
        return record;
    }

    /**
     * Maps the page's binary start to a SlottedPageHeader object.
     */
    public SlottedPageHeader getHeader() {
        return new SlottedPageHeader(
            readPageID(),
            readSlotCount(),
            getFreeSpacePointer(),
            getFreeSpaceEnd()
        );
    }

    /**
     * Maps a slot index to a PageSlotDirectory object.
     */
    public PageSlotDirectory getSlotDirectory(int slotNumber) {
        return new PageSlotDirectory(
            slotNumber,
            getSlotOffset(slotNumber),
            getSlotLength(slotNumber)
        );
    }
}
