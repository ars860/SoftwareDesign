package visitor;

import tokenizer.token.Token;

import java.util.List;

public interface TokenVisitor {
    void visitToken(Token token);
    void visitTokens(List<Token> tokens);
}
