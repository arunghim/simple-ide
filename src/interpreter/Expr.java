package interpreter;

public class Expr implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Term term;
    private Expr expr;
    private int opToken;

    public Expr(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        term = new Term(tokenizer, parser);
        term.parse();

        if (tokenizer.getToken() == Types.PLUS || tokenizer.getToken() == Types.MINUS) {
            opToken = tokenizer.getToken();
            tokenizer.skipToken();
            expr = new Expr(tokenizer, parser);
            expr.parse();
        }
    }

    @Override
    public int execute() {
        int leftValue = term.execute();
        if (expr == null) return leftValue;

        int rightValue = expr.execute();
        if (opToken == Types.PLUS) return leftValue + rightValue;
        if (opToken == Types.MINUS) return leftValue - rightValue;

        return leftValue;
    }

    @Override
    public void print(int indent) {
        term.print(indent);
        if (expr != null) {
            if (opToken == Types.PLUS) parser.out().print(" + ");
            else if (opToken == Types.MINUS) parser.out().print(" - ");
            expr.print(indent);
        }
    }
}
