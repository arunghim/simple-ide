package interpreter;

public interface ICore {
    void parse();

    int execute();

    void print(int indent);
}