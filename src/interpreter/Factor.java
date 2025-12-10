package interpreter;

public class Factor implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private int value;
    private String idName;
    private Expr expr;
    private boolean isExpr, isInt, isId;

    public Factor(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.isExpr = false;
        this.isInt = false;
        this.isId = false;
        this.value = 0;
    }

    @Override
    public void parse() {
        int token = tokenizer.getToken();

        if (token == Types.LEFT_PAREN) {
            tokenizer.skipToken();
            isExpr = true;
            expr = new Expr(tokenizer, parser);
            expr.parse();
            if (tokenizer.getToken() != Types.RIGHT_PAREN) throw new RuntimeException("ERROR: EXPECTED RIGHT PARENTHESIS TOKEN");
            tokenizer.skipToken();
        } else if (token == Types.INT_CONST) {
            isInt = true;
            value = tokenizer.intVal();
            tokenizer.skipToken();
        } else if (token == Types.ID) {
            isId = true;
            idName = tokenizer.idName();
            tokenizer.skipToken();
        } else throw new RuntimeException("ERROR: EXPECTED INT, ID, OR PARENTHESIS");
    }

    @Override
    public int execute() {
        if (isExpr) return expr.execute();
        else if (isInt) return value;
        else if (isId) {
            Id idManager = Id.getInstance(tokenizer);

            if (!idManager.isDeclared(idName)) throw new RuntimeException("ERROR: UNDECLARED IDENTIFIER '" + idName + "'");
            Integer val = idManager.getValue(idName);

            if (val == null) throw new RuntimeException("ERROR: VARIABLE '" + idName + "' USED BEFORE ASSIGNMENT");
            return val;
        }

        return 0;
    }

    @Override
    public void print(int indent) {
        if (isExpr) {
            System.out.print("(");
            expr.print(indent);
            System.out.print(")");
        } else if (isInt) System.out.print(value);
        else if (isId) System.out.print(idName);
    }
}
