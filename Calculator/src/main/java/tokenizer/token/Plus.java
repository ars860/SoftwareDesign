package tokenizer.token;

import java.util.function.BinaryOperator;

public class Plus extends Binary {
    static public Plus obj = new Plus();

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public BinaryOperator<Integer> operation() {
        return Integer::sum;
    }

    @Override
    public String show() {
        return "PLUS";
    }
}
