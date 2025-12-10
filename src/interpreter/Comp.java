package interpreter;

public class Comp implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Expr leftExpr, rightExpr;
    private int opToken;

    public Comp(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        leftExpr = new Expr(tokenizer, parser);
        leftExpr.parse();

        int token = tokenizer.getToken();
        if (token == Types.EQUALS || token == Types.LESS || token == Types.GREATER ||
                token == Types.LESS_EQUAL || token == Types.GREATER_EQUAL) {
            opToken = token;
            tokenizer.skipToken();
        } else throw new RuntimeException("ERROR: COMPARISON TOKEN EXPECTED");

        rightExpr = new Expr(tokenizer, parser);
        rightExpr.parse();
    }

    @Override
    public int execute() {
        boolean flag;

        if (opToken == Types.EQUALS) flag = leftExpr.execute() == rightExpr.execute();
        else if (opToken == Types.LESS) flag = leftExpr.execute() < rightExpr.execute();
        else if (opToken == Types.GREATER) flag = leftExpr.execute() > rightExpr.execute();
        else if (opToken == Types.LESS_EQUAL) flag = leftExpr.execute() <= rightExpr.execute();
        else if (opToken == Types.GREATER_EQUAL) flag = leftExpr.execute() >= rightExpr.execute();
        else throw new RuntimeException("ERROR: COMPARISON TOKEN EXPECTED");

        return flag ? 1 : 0;
    }

    @Override
    public void print(int indent) {
        leftExpr.print(0);
        switch (opToken) {
            case Types.EQUALS -> parser.out().print(" == ");
            case Types.LESS -> parser.out().print(" < ");
            case Types.GREATER -> parser.out().print(" > ");
            case Types.LESS_EQUAL -> parser.out().print(" <= ");
            case Types.GREATER_EQUAL -> parser.out().print(" >= ");
            default -> throw new RuntimeException("ERROR: UNKNOWN COMPARISON TOKEN");
        }
        rightExpr.print(0);
    }
}
