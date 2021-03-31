package tokenizer.token;

import java.util.function.BinaryOperator;

public class Div extends Binary {
    static public Div obj = new Div();

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public BinaryOperator<Integer> operation() {
        return (x, y) -> x / y;
    }

    @Override
    public String show() {
        return "DIV";
    }
}