package tokenizer.token;

public class Left extends Token {
    static public Left obj = new Left();

    @Override
    public String show() {
        return "LEFT";
    }
}
