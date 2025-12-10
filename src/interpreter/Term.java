package interpreter;

public class Term implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Factor factor;
    private Term term;
    private int opToken;

    public Term(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        factor = new Factor(tokenizer, parser);
        factor.parse();

        int token = tokenizer.getToken();
        if (token == Types.MULT || token == Types.DIV || token == Types.EXP) {
            opToken = token;
            tokenizer.skipToken();

            term = new Term(tokenizer, parser);
            term.parse();
        }
    }

    @Override
    public int execute() {
        int value = factor.execute();

        if (term != null) {
            switch (opToken) {
                case Types.MULT:
                    value *= term.execute();
                    break;
                case Types.DIV:
                    value /= term.execute();
                    break;
                case Types.EXP:
                    value = (int) Math.pow(value, term.execute());
                    break;
            }
        }

        return value;
    }

    public void print(int indent) {
        factor.print(indent);

        if (term != null) {
            switch (opToken) {
                case Types.MULT -> parser.out().print(" * ");
                case Types.DIV -> parser.out().print(" / ");
                case Types.EXP -> parser.out().print(" ^ ");
            }
            term.print(indent);
        }
    }
}
