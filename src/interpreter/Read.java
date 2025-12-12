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
        this.input = parser.inputScanner();
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

        int value;
        try {
            value = Integer.parseInt(currentLine);
        } catch (NumberFormatException e) {
            throw new RuntimeException("ERROR: EXPECTED INTEGER VALUE FOR '" + idName + "' BUT GOT '" + currentLine + "'", e);
        }

        idManager.setValue(idName, value);
        return 0;
    }

    @Override
    public void print(int indent) {
        parser.out().print(" ".repeat(indent) + idName);
    }
}
