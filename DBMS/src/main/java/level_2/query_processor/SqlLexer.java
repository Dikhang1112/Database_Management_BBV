package query_processor;
public class SqlLexer {
    private String sqlText;
    private int currentPosition;

    public SqlLexer(String sqlText) {
        this.sqlText = sqlText;
        this.currentPosition = 0;
    }

    public Token nextToken() {
        return null;
    }
}
