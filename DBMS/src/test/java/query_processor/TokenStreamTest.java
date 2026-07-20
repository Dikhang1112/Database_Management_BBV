package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class TokenStreamTest {

    @Test
    @DisplayName("TC-01. Create Token Stream")
    void createTokenStream_ShouldInitializeHeadPointer_WhenValidTokenListIsProvided() {
        // Arrange
        List<Token> tokens = Arrays.asList(
                new Token("KEYWORD", "SELECT"),
                new Token("IDENTIFIER", "id")
        );

        // Act
        TokenStream tokenStream = new TokenStream(tokens);

        // Assert
        assertThat(tokenStream.hasNext()).isTrue();
        assertThat(tokenStream.lookAhead(0)).isEqualTo(tokens.get(0));
    }

    @Test
    @DisplayName("TC-02. Consume Sequential Tokens")
    void consume_ShouldReturnNextToken_WhenMoreTokensExist() {
        // Arrange
        Token t1 = new Token("KEYWORD", "SELECT");
        Token t2 = new Token("IDENTIFIER", "name");
        TokenStream tokenStream = new TokenStream(Arrays.asList(t1, t2));

        // Act
        Token consumed1 = tokenStream.consume();
        Token consumed2 = tokenStream.consume();

        // Assert
        assertThat(consumed1).isEqualTo(t1);
        assertThat(consumed2).isEqualTo(t2);
        assertThat(tokenStream.hasNext()).isFalse();
    }

    @Test
    @DisplayName("TC-03. LookAhead Without Advancing")
    void lookAhead_ShouldReturnTokenWithoutAdvancingPointer_WhenOffsetIsValid() {
        // Arrange
        Token t1 = new Token("KEYWORD", "SELECT");
        Token t2 = new Token("IDENTIFIER", "age");
        TokenStream tokenStream = new TokenStream(Arrays.asList(t1, t2));

        // Act
        Token peekCurrent = tokenStream.lookAhead(0);
        Token peekNext = tokenStream.lookAhead(1);

        // Assert - headPointer remains at index 0
        assertThat(peekCurrent).isEqualTo(t1);
        assertThat(peekNext).isEqualTo(t2);
        assertThat(tokenStream.consume()).isEqualTo(t1);
    }

    @Test
    @DisplayName("TC-04. Empty Stream")
    void emptyStream_ShouldBeEmpty() {
        // Arrange
        TokenStream tokenStream = new TokenStream();

        // Assert
        assertThat(tokenStream.hasNext()).isFalse();
        assertThat(tokenStream.consume()).isNull();
    }

    @Test
    @DisplayName("TC-05. LookAhead Boundary")
    void lookAhead_ShouldReturnNull_WhenOffsetIsOutOfBounds() {
        // Arrange
        TokenStream tokenStream = new TokenStream(Collections.singletonList(new Token("KEYWORD", "WHERE")));

        // Assert
        assertThat(tokenStream.lookAhead(-1)).isNull();
        assertThat(tokenStream.lookAhead(1)).isNull();
        assertThat(tokenStream.lookAhead(99)).isNull();
    }

    @Test
    @DisplayName("TC-06. Consume After EOF")
    void consume_ShouldReturnNull_WhenConsumedPastEOF() {
        // Arrange
        TokenStream tokenStream = new TokenStream(Collections.singletonList(new Token("KEYWORD", "FROM")));
        tokenStream.consume(); // now at EOF

        // Act & Assert
        assertThat(tokenStream.hasNext()).isFalse();
        assertThat(tokenStream.consume()).isNull();
    }
}
