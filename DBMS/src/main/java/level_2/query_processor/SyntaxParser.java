package level_2.query_processor;

public class SyntaxParser {
    private SqlLexer lexerReference;

    public SyntaxParser(SqlLexer lexerReference) {
        this.lexerReference = lexerReference;
    }

    public AbstractSyntaxTree parseTree() {
        return null;
    }
}
