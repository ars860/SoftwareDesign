package tokenizer;

import tokenizer.token.*;
import tokenizer.token.Number;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final List<Token> tokens = new ArrayList<>();
    private State curState;

    public List<Token> tokenize(String s) throws TokenizerException {
        tokens.clear();
        curState = new DefaultState();

        for(Character ch : s.toCharArray()) {
            curState.processChar(ch);
        }
        curState.processEOF();

        return tokens;
    }

    private interface State {
        void processChar(char ch) throws TokenizerException;
        void processEOF();
    }

    private class DefaultState implements State {
        public DefaultState() {
        }

        DefaultState(char ch) throws TokenizerException {
            processChar(ch);
        }

        @Override
        public void processChar(char ch) throws TokenizerException {
            if (Character.isWhitespace(ch)) {
                return;
            }

            if (ch >= '0' && ch <= '9') {
                curState = new NumberState(ch - '0');
                return;
            }

            switch (ch) {
                case '+' -> tokens.add(Plus.obj);
                case '-' -> tokens.add(Minus.obj);
                case '/' -> tokens.add(Div.obj);
                case '*' -> tokens.add(Mul.obj);
                case '(' -> tokens.add(Left.obj);
                case ')' -> tokens.add(Right.obj);
                default -> throw new TokenizerException("Unknown character: " + ch);
            }
        }

        @Override
        public void processEOF() {}
    }

    private class NumberState implements State {
        int value;

        public NumberState(int value) {
            this.value = value;
        }

        @Override
        public void processChar(char ch) throws TokenizerException {
            if (ch >= '0' && ch <= '9') {
                value = value * 10 + (ch - '0');
                return;
            }

            tokens.add(new Number(value));
            curState = new DefaultState(ch);
        }

        @Override
        public void processEOF() {
            tokens.add(new Number(value));
        }
    }
}
