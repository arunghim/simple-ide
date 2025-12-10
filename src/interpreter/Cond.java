package interpreter;

public class Cond implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Comp comp;
    private Cond leftCond, rightCond;
    private int opToken;

    public Cond(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() == Types.LEFT_PAREN) {
            tokenizer.skipToken();

            if (tokenizer.getToken() == Types.EXCLAMATION) {
                tokenizer.skipToken();
                opToken = Types.EXCLAMATION;
                leftCond = new Cond(tokenizer, parser);
                leftCond.parse();
            } else {
                leftCond = new Cond(tokenizer, parser);
                leftCond.parse();

                if (tokenizer.getToken() == Types.AND || tokenizer.getToken() == Types.OR) {
                    opToken = tokenizer.getToken();
                    tokenizer.skipToken();
                    rightCond = new Cond(tokenizer, parser);
                    rightCond.parse();
                }
            }

            if (tokenizer.getToken() != Types.RIGHT_PAREN)
                throw new RuntimeException("ERROR: RIGHT PARENTHESIS TOKEN EXPECTED");
            tokenizer.skipToken();
        } else {
            comp = new Comp(tokenizer, parser);
            comp.parse();
        }
    }

    @Override
    public int execute() {
        if (comp != null) return comp.execute();

        int leftVal = leftCond.execute();
        if (opToken == Types.AND) return (leftVal != 0 && rightCond.execute() != 0) ? 1 : 0;
        else if (opToken == Types.OR) return (leftVal != 0 || rightCond.execute() != 0) ? 1 : 0;
        else if (opToken == Types.EXCLAMATION) return (leftVal == 0) ? 1 : 0;

        return leftVal;
    }

    @Override
    public void print(int indent) {
        if (opToken == Types.EXCLAMATION) {
            parser.out().print("!(");
            leftCond.print(0);
            parser.out().print(")");
        } else if (opToken == Types.AND) {
            parser.out().print("(");
            leftCond.print(0);
            parser.out().print(" && ");
            rightCond.print(0);
            parser.out().print(")");
        } else if (opToken == Types.OR) {
            parser.out().print("(");
            leftCond.print(0);
            parser.out().print(" || ");
            rightCond.print(0);
            parser.out().print(")");
        } else {
            if (comp != null) comp.print(0);
            else leftCond.print(indent);
        }
    }
}
