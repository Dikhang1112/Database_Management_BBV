package database_core_server;

import database_core_server.interfaces.DdlCompiler;
import database_core_server.interfaces.MetadataEntity;

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
