package interpreter;

public class StmtSeq implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private StmtSeq stmtSeq;
    private Stmt stmt;
    private boolean hasStmtSeq;

    public StmtSeq(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        hasStmtSeq = false;
    }

    @Override
    public void parse() {
        stmt = new Stmt(tokenizer, parser);
        stmt.parse();

        int token = tokenizer.getToken();
        if (token != Types.END && token != Types.ELSE && token != Types.LOOP) {
            stmtSeq = new StmtSeq(tokenizer, parser);
            stmtSeq.parse();
            hasStmtSeq = true;
        }
    }

    @Override
    public int execute() {
        stmt.execute();
        if (hasStmtSeq) stmtSeq.execute();

        return 0;
    }

    @Override
    public void print(int indent) {
        stmt.print(indent);
        if (hasStmtSeq) stmtSeq.print(indent);
    }
}
