package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Words {

    public static Map<String, Integer> initialize() {
        Map<String, Integer> tokens = new HashMap<>();

        tokens.put("PROGRAM", Types.PROGRAM);
        tokens.put("BEGIN", Types.BEGIN);
        tokens.put("END", Types.END);
        tokens.put("INT", Types.INT);
        tokens.put("IF", Types.IF);
        tokens.put("THEN", Types.THEN);
        tokens.put("ELSE", Types.ELSE);
        tokens.put("WHILE", Types.WHILE);
        tokens.put("LOOP", Types.LOOP);
        tokens.put("READ", Types.READ);
        tokens.put("WRITE", Types.WRITE);
        tokens.put("FOR", Types.FOR);
        return tokens;
    }
}
