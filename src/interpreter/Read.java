package interpreter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Read implements ICore {
    private static Scanner input;
    private final Tokenizer tokenizer;
    private String idName;

    public Read(Tokenizer tokenizer, Parser parser) {
        this.tokenizer = tokenizer;

        if (input == null)
            try {
                input = new Scanner(new FileReader(parser.data()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("ERROR: FAILED TO READ FROM DATA FILE", e);
            }
    }

    @Override
    public void parse() {
        if (tokenizer.getToken() != Types.ID) throw new RuntimeException("ERROR: EXPECTED IDENTIFIER");

        idName = tokenizer.idName();
        tokenizer.skipToken();
    }

    @Override
    public int execute() {
        if (input == null) throw new RuntimeException("ERROR: NO INPUT FILE AVAILABLE");

        Id idManager = Id.getInstance(tokenizer);
        if (!idManager.isDeclared(idName)) idManager.declare(idName);

        if (!input.hasNextLine()) throw new RuntimeException("ERROR: NO MORE LINES IN THE INPUT FILE");
        String currentLine = input.nextLine().trim();

        try {
            int value = Integer.parseInt(currentLine);
            idManager.setValue(idName, value);
        } catch (NumberFormatException e){
            throw new RuntimeException("ERROR: EXPECTED INTEGER VALUE FOR '" + idName + "' BUT GOT '" + currentLine + "'", e);
        }

        return 0;
    }

    @Override
    public void print(int indent) {
        System.out.print("read " + idName + ";");
    }
}
