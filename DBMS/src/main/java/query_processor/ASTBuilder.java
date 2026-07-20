package query_processor;

public class ASTBuilder {
    public AST buildAST(ParseTree parseTree) {
        if (parseTree == null || parseTree.getRootNode() == null) {
            return new AST();
        }
        return new AST(mapToLogicalNode(parseTree.getRootNode()));
    }

    private ASTNode mapToLogicalNode(ParseTreeNode node) {
        return new ASTNode() {};
    }
}
