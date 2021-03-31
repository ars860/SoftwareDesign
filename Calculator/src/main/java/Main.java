import tokenizer.Tokenizer;
import tokenizer.TokenizerException;
import tokenizer.token.Token;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;
import visitor.TokenVisitor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected 1 argument.");
            return;
        }

        String input = args[0];
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens;

        try {
            tokens = tokenizer.tokenize(input);
        } catch (TokenizerException e) {
            System.out.println("Error in expression");
            return;
        }

        TokenVisitor printer = new PrintVisitor();
        printer.visitTokens(tokens);

        ParserVisitor parser = new ParserVisitor();
        parser.visitTokens(tokens);
        tokens = parser.getOutput();

        printer.visitTokens(tokens);

        CalcVisitor calcVisitor = new CalcVisitor();
        calcVisitor.visitTokens(tokens);
        System.out.println("Result: " + calcVisitor.getResult());
    }
}
