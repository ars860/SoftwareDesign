package visitor;

import tokenizer.token.*;
import tokenizer.token.Number;

import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    Stack<Token> stack = new Stack<>();
    Stack<Token> output = new Stack<>();

    public Stack<Token> getOutput() {
        return output;
    }

    private void visitNumber(Number number) {
        output.add(number);
    }

    private void visitBinary(Binary binary) {
        while (!stack.empty() &&
                stack.lastElement() instanceof Binary &&
                ((Binary) stack.lastElement()).getPriority() >= binary.getPriority()
        ) {
            output.add(stack.lastElement());
            stack.pop();
        }

        stack.add(binary);
    }

    private void visitLeft(Left left) {
        stack.add(left);
    }

    private void visitRight() {
        while (stack.size() > 0 && !(stack.lastElement() instanceof Left)) {
            output.add(stack.lastElement());
            stack.pop();
        }

        if (!stack.empty() && stack.lastElement() instanceof Left) {
            stack.pop();
        } else {
            throw new RuntimeException("Bad brackets");
        }
    }

    @Override
    public void visitToken(Token token) {
        if (token instanceof Number) {
            visitNumber((Number) token);
        }

        if (token instanceof Binary) {
            visitBinary((Binary) token);
        }

        if (token instanceof Left) {
            visitLeft((Left) token);
        }

        if (token instanceof Right) {
            visitRight();
        }
    }

    @Override
    public void visitTokens(List<Token> tokens) {
        for (Token token : tokens) {
            visitToken(token);
        }

        while (!stack.empty()) {
            if (stack.lastElement() instanceof Binary) {
                output.add(stack.lastElement());
                stack.pop();
            } else {
                throw new RuntimeException("Bad tail");
            }
        }
    }
}
