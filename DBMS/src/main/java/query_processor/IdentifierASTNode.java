package query_processor;

public class IdentifierASTNode extends ASTNode {
    private String columnName;

    public IdentifierASTNode() {
        super("IdentifierASTNode");
    }

    public IdentifierASTNode(String columnName) {
        super("IdentifierASTNode");
        this.columnName = columnName;
    }

    public String getValue() {
        return columnName;
    }
}
