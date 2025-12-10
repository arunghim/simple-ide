package interpreter;

public class DeclSeq implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private DeclSeq declSeq;
    private Decl decl;
    private boolean hasDeclSeq;

    public DeclSeq(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.hasDeclSeq = false;
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() == Types.INT) tokenizer.skipToken();

        decl = new Decl(tokenizer);
        decl.parse();

        if (tokenizer.getToken() == Types.COMMA) {
            tokenizer.skipToken();
            hasDeclSeq = true;
            declSeq = new DeclSeq(tokenizer, parser);
            declSeq.parse();
        }

        if (tokenizer.getToken() == Types.SEMICOLON) tokenizer.skipToken();

        if (tokenizer.getToken() == Types.INT) {
            hasDeclSeq = true;
            declSeq = new DeclSeq(tokenizer, parser);
            declSeq.parse();
        }
    }

    @Override
    public int execute() {
        decl.execute();
        if (hasDeclSeq) declSeq.execute();

        return 0;
    }

    @Override
    public void print(int indent) {
        decl.print(0);
        if (hasDeclSeq) {
            System.out.print(", ");
            declSeq.print(0);
        } else System.out.println(";");
    }
}
