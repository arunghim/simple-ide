package interpreter;

public class For implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Assign firstAssign;
    private Assign secondAssign;
    private Cond cond;
    private StmtSeq stmtSeq;

    public For(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
    }

    @Override
    public void parse() {
        tokenizer.skipToken();

        if (tokenizer.getToken() != Types.LEFT_PAREN) throw new RuntimeException("ERROR: LEFT PAREN TOKEN EXPECTED");
        tokenizer.skipToken();

        firstAssign = new Assign(tokenizer, parser);
        firstAssign.parse();

        cond = new Cond(tokenizer, parser);
        cond.parse();

        if (tokenizer.getToken() != Types.SEMICOLON) throw new RuntimeException("ERROR: SEMICOLON TOKEN EXPECTED");
        tokenizer.skipToken();

        secondAssign = new Assign(tokenizer, parser, false);
        secondAssign.parse();

        if (tokenizer.getToken() != Types.RIGHT_PAREN) throw new RuntimeException("ERROR: RIGHT PAREN TOKEN EXPECTED");
        tokenizer.skipToken();

        if (tokenizer.getToken() != Types.LOOP) throw new RuntimeException("ERROR: LOOP TOKEN EXPECTED");
        tokenizer.skipToken();

        stmtSeq = new StmtSeq(tokenizer, parser);
        stmtSeq.parse();

        if (tokenizer.getToken() != Types.END) throw new RuntimeException("ERROR: END TOKEN EXPECTED");
        tokenizer.skipToken();

        if (tokenizer.getToken() != Types.SEMICOLON) throw new RuntimeException("ERROR: SEMICOLON TOKEN EXPECTED");
        tokenizer.skipToken();
    }

    @Override
    public int execute() {
        for (firstAssign.execute(); cond.execute() != 0; secondAssign.execute()) {
            stmtSeq.execute();
        }

        return 0;
    }

    @Override
    public void print(int indent) {
        String indentation = " ".repeat(indent);

        parser.out().print(indentation + "for (");
        firstAssign.print(0);
        cond.print(0);
        parser.out().println(")");
        stmtSeq.print(indent + 2);
        parser.out().println(indentation + "end;");
    }
}
