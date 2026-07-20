package query_processor;

public class AST {
    private ASTNode rootASTNode;

    public AST() {
    }

    public AST(ASTNode rootASTNode) {
        this.rootASTNode = rootASTNode;
    }

    public ASTNode getRootASTNode() {
        return rootASTNode;
    }
}
