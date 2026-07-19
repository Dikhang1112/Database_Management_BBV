package level_3.query_processor;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {
    private List<Token> tokenList;
    private int headPointer;

    public TokenStream() {
        this.tokenList = new ArrayList<>();
        this.headPointer = 0;
    }

    public TokenStream(List<Token> tokenList) {
        this.tokenList = tokenList != null ? tokenList : new ArrayList<>();
        this.headPointer = 0;
    }

    public boolean hasNext() {
        return headPointer < tokenList.size();
    }

    public Token consume() {
        if (!hasNext()) {
            return null;
        }
        return tokenList.get(headPointer++);
    }

    public Token lookAhead(int offset) {
        int targetIndex = headPointer + offset;
        if (targetIndex < 0 || targetIndex >= tokenList.size()) {
            return null;
        }
        return tokenList.get(targetIndex);
    }
}
