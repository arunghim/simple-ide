package interpreter;

import java.util.ArrayList;

public class Write implements ICore {
    private final Tokenizer tokenizer;
    private final Parser parser;
    private final ArrayList<String> idNames;

    public Write(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.idNames = new ArrayList<>();
    }

    @Override
    public void parse() {
        tokenizer.skipToken();

        if (tokenizer.getToken() != Types.ID) throw new RuntimeException("ERROR: EXPECTED IDENTIFIER AFTER 'write'");

        while (true) {
            if (tokenizer.getToken() != Types.ID) throw new RuntimeException("ERROR: ID TOKEN EXPECTED");

            idNames.add(tokenizer.idName());
            tokenizer.skipToken();

            if (tokenizer.getToken() == Types.COMMA) tokenizer.skipToken();
            else break;
        }

        if (tokenizer.getToken() != Types.SEMICOLON)
            throw new RuntimeException("ERROR: SEMICOLON ';' EXPECTED AFTER WRITE STATEMENT");

        tokenizer.skipToken();
    }


    @Override
    public int execute() {
        Id idManager = Id.getInstance(tokenizer, parser);

        for (String idName : idNames) {
            if (!idManager.isDeclared(idName))
                throw new RuntimeException("ERROR: UNDECLARED IDENTIFIER '" + idName + "'");

            Integer value = idManager.getValue(idName);
            if (value == null) throw new RuntimeException("ERROR: IDENTIFIER '" + idName + "' USED BEFORE ASSIGNMENT");

            System.out.println(idName + " = " + value);
        }

        return 0;
    }

    @Override
    public void print(int indent) {
        String indentation = " ".repeat(indent);
        parser.out().print(indentation + "write ");
        for (int i = 0; i < idNames.size(); i++) {
            parser.out().print(idNames.get(i));
            if (i < idNames.size() - 1) parser.out().print(", ");
        }
        parser.out().println(";");
    }
}
