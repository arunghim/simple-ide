package interpreter;

import java.io.PrintStream;
import java.util.Scanner;

public class Parser {
    private final Tokenizer tokenizer;
    private final String data;
    private Prog prog;
    private final PrintStream out;
    private Scanner inputScanner;

    public Parser(String program, String data, PrintStream out) {
        Id.reset();
        this.tokenizer = new Tokenizer(program);
        this.data = data;
        this.out = out;
        build();
        resetScanner();
    }

    private void build() {
        if (tokenizer.getToken() != Types.PROGRAM)
            throw new RuntimeException("ERROR: PROGRAM TOKEN EXPECTED");
        prog = new Prog(tokenizer, this);
    }

    public String data() {
        return data;
    }

    public PrintStream out() {
        return out;
    }

    public void parse() {
        prog.parse();
    }

    public void print() {
        prog.print(0);
    }

    public void execute() {
        prog.execute();
    }

    public Scanner inputScanner() {
        return inputScanner;
    }

    public void resetScanner() {
        inputScanner = new Scanner(data);
    }
}
