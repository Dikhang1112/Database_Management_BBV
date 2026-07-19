package level_3.query_processor;

public class BinaryOpASTNode extends ASTNode {
    private String operatorType;
    private ASTNode leftNode;
    private ASTNode rightNode;

    public BinaryOpASTNode() {
        super("BinaryOpASTNode");
    }

    public BinaryOpASTNode(String operatorType, ASTNode leftNode, ASTNode rightNode) {
        super("BinaryOpASTNode");
        this.operatorType = operatorType;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public ASTNode getLeftNode() {
        return leftNode;
    }

    public ASTNode getRightNode() {
        return rightNode;
    }
}
