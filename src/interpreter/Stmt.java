package interpreter;

public class Stmt implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private ReadSeq codeReadSeq;
    private ICore stmt;

    public Stmt(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        int token = tokenizer.getToken();

        if (token == Types.IF) {
            If codeIf = new If(tokenizer, parser);
            codeIf.parse();
            stmt = codeIf;
        } else if (token == Types.WHILE) {
            While codeWhile = new While(tokenizer, parser);
            codeWhile.parse();
            stmt = codeWhile;
        } else if (token == Types.READ) {
            codeReadSeq = new ReadSeq(tokenizer, parser);
            codeReadSeq.parse();
            stmt = codeReadSeq;
        } else if (token == Types.WRITE) {
            Write codeWrite = new Write(tokenizer, parser);
            codeWrite.parse();
            stmt = codeWrite;
        } else if (token == Types.ID) {
            Assign coreAssign = new Assign(tokenizer, parser);
            coreAssign.parse();
            stmt = coreAssign;
        } else if (token == Types.FOR) {
            For codeFor = new For(tokenizer, parser);
            codeFor.parse();
            stmt = codeFor;
        } else {
            if (tokenizer.getToken() != Types.EOF || tokenizer.getToken() != Types.ELSE)
                throw new RuntimeException("ERROR: INVALID STATEMENT TOKEN");
        }
    }

    @Override
    public int execute() {
        stmt.execute();
        return 0;
    }

    @Override
    public void print(int indent) {
        if (!stmt.equals(codeReadSeq)) stmt.print(indent + 2);
        else {
            parser.out().print(" ".repeat(indent + 2) + "read ");
            stmt.print(indent + 2);
        }
    }
}
