package tokenizer.token;

import visitor.TokenVisitor;

abstract public class Token {
    public abstract String show();

    public void accept(TokenVisitor visitor) {
        visitor.visitToken(this);
    }
}
