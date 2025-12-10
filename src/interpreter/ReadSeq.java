package interpreter;

public class ReadSeq implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private Read read;
    private ReadSeq readSeq;
    private boolean hasReadSeq;

    public ReadSeq(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.hasReadSeq = false;
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() == Types.READ) tokenizer.skipToken();

        read = new Read(tokenizer, parser);
        read.parse();

        if (tokenizer.getToken() == Types.COMMA) {
            tokenizer.skipToken();
            hasReadSeq = true;
            readSeq = new ReadSeq(tokenizer, parser);
            readSeq.parse();
        }

        if (tokenizer.getToken() == Types.SEMICOLON) tokenizer.skipToken();

        if (tokenizer.getToken() == Types.READ) {
            hasReadSeq = true;
            readSeq = new ReadSeq(tokenizer, parser);
            readSeq.parse();
        }
    }

    @Override
    public int execute() {
        if (read != null) read.execute();
        if (hasReadSeq && readSeq != null) readSeq.execute();

        return 0;
    }

    @Override
    public void print(int indent) {
        read.print(indent);
        if (hasReadSeq) {
            parser.out().print(", ");
            readSeq.print(indent);
        } else {
            parser.out().println(";");
        }
    }
}
