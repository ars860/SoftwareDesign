package tokenizer.token;

import java.util.function.BinaryOperator;

public class Mul extends Binary {
    static public Mul obj = new Mul();

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public BinaryOperator<Integer> operation() {
        return (x, y) -> x * y;
    }

    @Override
    public String show() {
        return "MUL";
    }
}
