package level_2.database_core_server;

import level_2.database_core_server.interfaces.DdlCompiler;
import level_2.database_core_server.interfaces.MetadataEntity;

public class MetadataDdlCompiler implements DdlCompiler {
    private ObjectMetadataCache cacheReference;
    private int currentAutoIncrementID;

    public MetadataDdlCompiler(ObjectMetadataCache cacheReference, int currentAutoIncrementID) {
        this.cacheReference = cacheReference;
        this.currentAutoIncrementID = currentAutoIncrementID;
    }

    @Override
    public MetadataEntity compileDdl(String sqlText) {
        return null;
    }
}
