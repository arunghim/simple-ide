package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Symbols {

    public static Map<String, Integer> initialize() {
        Map<String, Integer> symbols = new HashMap<>();

        symbols.put(";", Types.SEMICOLON);
        symbols.put(",", Types.COMMA);
        symbols.put("=", Types.ASSIGN);
        symbols.put("!", Types.EXCLAMATION);
        symbols.put("[", Types.LEFT_BRACKET);
        symbols.put("]", Types.RIGHT_BRACKET);
        symbols.put("&&", Types.AND);
        symbols.put("||", Types.OR);
        symbols.put("(", Types.LEFT_PAREN);
        symbols.put(")", Types.RIGHT_PAREN);
        symbols.put("+", Types.PLUS);
        symbols.put("-", Types.MINUS);
        symbols.put("*", Types.MULT);
        symbols.put("/", Types.DIV);
        symbols.put("^", Types.EXP);
        symbols.put("!=", Types.NOT_EQUALS);
        symbols.put("==", Types.EQUALS);
        symbols.put("<", Types.LESS);
        symbols.put(">", Types.GREATER);
        symbols.put("<=", Types.LESS_EQUAL);
        symbols.put(">=", Types.GREATER_EQUAL);

        return symbols;
    }
}
