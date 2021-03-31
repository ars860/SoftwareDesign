package tokenizer.token;

import java.util.function.BinaryOperator;

public class Minus extends Binary {
    static public Minus obj = new Minus();

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public BinaryOperator<Integer> operation() {
        return (x, y) -> x - y;
    }

    @Override
    public String show() {
        return "MINUS";
    }
}
