package interpreter;

import java.util.Scanner;

public class Read implements ICore {
    private final Scanner input;
    private final Tokenizer tokenizer;
    private final Parser parser;

    private String idName;

    public Read(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.input = new Scanner(parser.data());
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() != Types.ID) throw new RuntimeException("ERROR: EXPECTED IDENTIFIER");
        idName = tokenizer.idName();
        tokenizer.skipToken();
    }

    @Override
    public int execute() {
        Id idManager = Id.getInstance(tokenizer, parser);
        if (!idManager.isDeclared(idName)) idManager.declare(idName);

        if (!input.hasNextLine()) throw new RuntimeException("ERROR: NO MORE LINES IN THE INPUT");
        String currentLine = input.nextLine().trim();

        try {
            int value = Integer.parseInt(currentLine);
            idManager.setValue(idName, value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("ERROR: EXPECTED INTEGER VALUE FOR '" + idName + "' BUT GOT '" + currentLine + "'", e);
        }

        return 0;
    }

    @Override
    public void print(int indent) {
        parser.out().print("read " + idName + ";");
    }
}
