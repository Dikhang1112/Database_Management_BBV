package query_processor;

public class LiteralASTNode extends ASTNode {
    private String rawLiteralValue;
    private String inferredDataType;

    public LiteralASTNode() {
        super("LiteralASTNode");
    }

    public LiteralASTNode(String rawLiteralValue, String inferredDataType) {
        super("LiteralASTNode");
        this.rawLiteralValue = rawLiteralValue;
        this.inferredDataType = inferredDataType;
    }

    public String getValue() {
        return rawLiteralValue;
    }

    public String getInferredType() {
        return inferredDataType;
    }
}
