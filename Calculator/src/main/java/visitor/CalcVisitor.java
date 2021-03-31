package visitor;

import tokenizer.token.Binary;
import tokenizer.token.Number;
import tokenizer.token.Token;

import java.util.List;
import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    Stack<Integer> stack = new Stack<>();

    public int getResult() {
        return stack.get(0);
    }

    @Override
    public void visitToken(Token token) {
        if (token instanceof Number) {
            stack.push(((Number) token).getValue());
            return;
        }

        if (token instanceof Binary) {
            int y = stack.pop();
            int x = stack.pop();
            stack.push(((Binary) token).operation().apply(x, y));
            return;
        }

        throw new RuntimeException("Bad tokens");
    }

    @Override
    public void visitTokens(List<Token> tokens) {
        for (Token token : tokens) {
            visitToken(token);
        }
    }
}
