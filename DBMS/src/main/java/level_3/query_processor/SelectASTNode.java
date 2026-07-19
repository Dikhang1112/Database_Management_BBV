package level_3.query_processor;

import java.util.List;

public class SelectASTNode extends ASTNode {
    private String tableName;
    private List<String> projectionFields;
    private ASTNode whereCondition;

    public SelectASTNode() {
        super("SelectASTNode");
    }

    public SelectASTNode(String tableName, List<String> projectionFields, ASTNode whereCondition) {
        super("SelectASTNode");
        this.tableName = tableName;
        this.projectionFields = projectionFields;
        this.whereCondition = whereCondition;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getProjectionFields() {
        return projectionFields;
    }

    public ASTNode getWhereCondition() {
        return whereCondition;
    }
}
