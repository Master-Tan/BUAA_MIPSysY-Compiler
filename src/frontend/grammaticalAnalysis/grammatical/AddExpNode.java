package frontend.grammaticalAnalysis.grammatical;

import frontend.grammaticalAnalysis.SymbolTable;
import frontend.lexicalAnalysis.lexical.Word;

public class AddExpNode extends Node {

    private AddExpNode addExpNode;
    private Word separator;
    private MulExpNode mulExpNode;

    public AddExpNode(MulExpNode mulExpNode, int line) {
        super(line);
        this.addExpNode = null;
        this.separator = null;
        this.mulExpNode = mulExpNode;
    }

    public AddExpNode(AddExpNode addExpNode, Word separator, MulExpNode mulExpNode, int line) {
        super(line);
        this.addExpNode = addExpNode;
        this.separator = separator;
        this.mulExpNode = mulExpNode;
    }

    public AddExpNode getAddExpNode() {
        return addExpNode;
    }

    public Word getSeparator() {
        return separator;
    }

    public MulExpNode getMulExpNode() {
        return mulExpNode;
    }

    public DataType getDataType(SymbolTable currentSymbolTable) {
        return mulExpNode.getDataType(currentSymbolTable);
    }
}
