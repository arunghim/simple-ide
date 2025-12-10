package interpreter;

public class Assign implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Expr expr;
    private String idName;
    private final boolean requireSemicolon;

    public Assign(Tokenizer tokenizer, Parser parser) {
        this(tokenizer, parser, true);
    }

    public Assign(Tokenizer tokenizer, Parser parser, boolean requireSemicolon) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.requireSemicolon = requireSemicolon;
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() != Types.ID) throw new RuntimeException("ERROR: ID TOKEN EXPECTED");

        idName = tokenizer.idName();
        tokenizer.skipToken();

        if (tokenizer.getToken() != Types.ASSIGN) throw new RuntimeException("ERROR: ASSIGNMENT '=' TOKEN EXPECTED");
        tokenizer.skipToken();

        expr = new Expr(tokenizer, parser);
        expr.parse();

        if (requireSemicolon) {
            if (tokenizer.getToken() != Types.SEMICOLON) throw new RuntimeException("ERROR: SEMICOLON ';' EXPECTED");
            tokenizer.skipToken();
        }
    }

    @Override
    public int execute() {
        int value = expr.execute();

        Id idManager = Id.getInstance(tokenizer);
        if (!idManager.isDeclared(idName)) throw new RuntimeException("ERROR: ID '" + idName + "' NOT DECLARED");

        idManager.setValue(idName, value);
        return value;
    }

    @Override
    public void print(int indent) {
        String indentation = " ".repeat(indent);
        System.out.print(indentation + idName + " = ");
        expr.print(0);
        System.out.println(";");
    }
}
