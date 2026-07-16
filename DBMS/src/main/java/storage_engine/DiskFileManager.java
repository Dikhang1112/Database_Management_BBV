package storage_engine;
import java.io.IOException;

public class DiskFileManger {
    protected String dataDirectoryPath;
    public DiskFileManger(String dataDirectoryPath){
        this.dataDirectoryPath = dataDirectoryPath;
    }

    public byte[] readPageFromDisk(int pageId) {
        return new byte[0];
    }
    public void writePageToDisk(int pageID, byte[] pageData){}

}
