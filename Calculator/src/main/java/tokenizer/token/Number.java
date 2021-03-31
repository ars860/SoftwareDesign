package tokenizer.token;

public class Number extends Token {
    int value;

    public int getValue() {
        return value;
    }

    public Number(int value) {
        this.value = value;
    }

    @Override
    public String show() {
        return "Number(" + value + ")";
    }
}
