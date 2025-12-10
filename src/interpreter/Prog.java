package interpreter;

public class Prog implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private DeclSeq declSeq;
    private StmtSeq stmtSeq;

    public Prog(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() != Types.PROGRAM) throw new RuntimeException("ERROR: PROGRAM TOKEN EXPECTED");
        tokenizer.skipToken();

        declSeq = new DeclSeq(tokenizer, parser);
        declSeq.parse();

        if (tokenizer.getToken() != Types.BEGIN) throw new RuntimeException("ERROR: BEGIN TOKEN EXPECTED");
        tokenizer.skipToken();

        stmtSeq = new StmtSeq(tokenizer, parser);
        stmtSeq.parse();

        if (tokenizer.getToken() != Types.END) throw new RuntimeException("ERROR: END TOKEN EXPECTED");
        tokenizer.skipToken();

        if (tokenizer.getToken() != Types.EOF) throw new RuntimeException("ERROR: EOF TOKEN EXPECTED");
    }

    @Override
    public int execute() {
        declSeq.execute();
        stmtSeq.execute();

        return 0;
    }

    @Override
    public void print(int indent) {
        parser.out().println();
        String indentStr = " ".repeat(indent);
        parser.out().println(indentStr + "program");
        parser.out().print(indentStr + "int ");
        declSeq.print(indent + 1);
        parser.out().println(indentStr + "begin");
        stmtSeq.print(indent + 1);
        parser.out().println(indentStr + "end");
    }
}
