import interpreter.Parser;

void main () {
    try (Scanner sc = new Scanner(System.in)) {
        IO.print("Enter path to code file: ");
        String codePath = sc.nextLine();
        IO.print("Enter path to data file: ");
        String dataPath = sc.nextLine();

        Parser parser = new Parser(codePath, dataPath);

        parser.parse();
        parser.execute();
        parser.print();
    }}