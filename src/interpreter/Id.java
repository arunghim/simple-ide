package interpreter;

import java.util.HashMap;

public class Id implements ICore {
    private static Id instance;
    private final HashMap<String, Integer> identifiers = new HashMap<>();
    private final Tokenizer tokenizer;
    private String idName;

    private Id(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public static Id getInstance(Tokenizer tokenizer) {
        if (instance == null)
            instance = new Id(tokenizer);
        return instance;
    }

    @Override
    public void parse() {
        while (tokenizer.getToken() != Types.SEMICOLON) {
            if (tokenizer.getToken() == Types.ID) idName = tokenizer.idName();

            if (tokenizer.getToken() == Types.ID) {
                declare(idName);
                tokenizer.skipToken();
            } else if (tokenizer.getToken() == Types.COMMA) tokenizer.skipToken();
            else throw new RuntimeException("ERROR: Unexpected token '" + tokenizer.getToken() + "' in identifier list.");
        }

        tokenizer.skipToken();
    }

    @Override
    public int execute() {
        if (!identifiers.containsKey(idName)) throw new RuntimeException("ERROR: Undeclared identifier '" + idName + "'");

        return identifiers.get(idName);
    }

    @Override
    public void print(int indent) {
        System.out.print(idName);
    }

    public void declare(String name) {
        if (identifiers.containsKey(name)) throw new RuntimeException("ERROR: Duplicate identifier '" + name + "'");

        identifiers.put(name, null);
    }

    public void setValue(String name, int value) {
        if (!identifiers.containsKey(name)) throw new RuntimeException("ERROR: Identifier not declared '" + name + "'");
        identifiers.put(name, value);
    }

    public Integer getValue(String name) {
        return identifiers.get(name);
    }

    public boolean isDeclared(String name) {
        return identifiers.containsKey(name);
    }
}
