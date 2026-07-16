package level_3.storage_engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SlottedPageFormatterTest {

    private byte[] buffer;
    private SlottedPageFormatter formatter;
    private final int PAGE_SIZE = 128; // Small page size for edge case testing

    @BeforeEach
    public void setUp() {
        buffer = new byte[PAGE_SIZE];
        formatter = new SlottedPageFormatter(buffer);
    }

    @Test
    public void testCase1_WriteHeaderAndInitialization_Success() {
        formatter.writeHeader(99, 0);

        assertEquals(99, formatter.readPageID(), "PageID should match 99");
        assertEquals(0, formatter.readSlotCount(), "Initial slot count should be 0");
        assertEquals(16, formatter.getFreeSpacePointer(), "FreeSpacePointer should start after Header");
        assertEquals(PAGE_SIZE, formatter.getFreeSpaceEnd(), "FreeSpaceEnd should point to the end of the page");
    }

    @Test
    public void testCase2_CheckAvailableSpace_Success() {
        formatter.writeHeader(99, 0);
        assertTrue(formatter.hasSpaceFor(20), "Page should have space for a 20-byte record");
    }

    @Test
    public void testCase3_And_Case4_InsertAndReadRecord_Success() {
        formatter.writeHeader(99, 0);
        byte[] recordBytes = {0x11, 0x22, 0x33, 0x44}; // 4 bytes of data

        int insertedOffset = formatter.insertRecordAtEnd(recordBytes);
        formatter.writeSlot(0, insertedOffset, recordBytes.length);

        assertEquals(124, insertedOffset, "Record should be at offset 124");
        assertEquals(124, formatter.getSlotOffset(0), "Slot 0 should point to offset 124");
        assertEquals(4, formatter.getSlotLength(0), "Slot 0 should store length 4");

        assertEquals(24, formatter.getFreeSpacePointer(), "Pointer should move to byte 24");
        assertEquals(124, formatter.getFreeSpaceEnd(), "End offset should move to 124");

        byte[] retrievedData = formatter.readRecordFromOffset(insertedOffset, recordBytes.length);
        assertArrayEquals(recordBytes, retrievedData, "Retrieved data should match the inserted data");

        assertEquals(0x11, buffer[124], "Byte at offset 124 should be 0x11");
        assertEquals(0x44, buffer[127], "Byte at offset 127 should be 0x44");
    }

    @Test
    public void testCase5_PageOverflowPrevention_ShouldReject() {
        formatter.writeHeader(99, 0);
        byte[] giantRecord = new byte[110]; // Too large for the page

        assertFalse(formatter.hasSpaceFor(giantRecord.length), "hasSpaceFor should return false for large records");
        int errorCode = formatter.insertRecordAtEnd(giantRecord);
        assertEquals(-1, errorCode, "insertRecordAtEnd should return error code -1 on overflow");
    }
}