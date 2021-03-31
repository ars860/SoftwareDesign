package tokenizer.token;

import java.util.function.BinaryOperator;

abstract public class Binary extends Token {
    public abstract int getPriority();
    public abstract BinaryOperator<Integer> operation();
}
