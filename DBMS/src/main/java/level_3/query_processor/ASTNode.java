package level_3.query_processor;

public abstract class ASTNode {
    private String nodeName;

    public ASTNode() {
    }

    public ASTNode(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }
}
