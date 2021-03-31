package visitor;

import tokenizer.token.Token;

import java.util.List;

public class PrintVisitor implements TokenVisitor {
    @Override
    public void visitToken(Token token) {
        System.out.println(token.show());
    }

    @Override
    public void visitTokens(List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.show()).append(" ");
        }
        System.out.println(sb.toString());
    }
}
