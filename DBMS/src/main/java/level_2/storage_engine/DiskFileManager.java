package level_2.storage_engine;

public class DiskFileManager {
    private String dataDirectoryPath;

    public DiskFileManager(String dataDirectoryPath) {
        this.dataDirectoryPath = dataDirectoryPath;
    }

    public byte[] readPageFromDisk(int pageID) {
        return new byte[0];
    }

    public void writePageToDisk(int pageID, byte[] pageData) {
    }
}
