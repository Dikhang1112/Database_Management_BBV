package query_processor;

import database_core_server.interfaces.MetadataCache;
import database_core_server.interfaces.SessionContext;

public class QueryValidator {
    private MetadataCache metadataCacheReference;
    private boolean isSemantic;

    public QueryValidator(MetadataCache metadataCacheReference, boolean isSemantic) {
        this.metadataCacheReference = metadataCacheReference;
        this.isSemantic = isSemantic;
    }

    public boolean validateAst(AbstractSyntaxTree ast, SessionContext session) {
        return false;
    }
}
