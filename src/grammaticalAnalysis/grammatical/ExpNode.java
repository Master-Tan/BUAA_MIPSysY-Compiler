package grammaticalAnalysis.grammatical;

import grammaticalAnalysis.SymbolTable;

public class ExpNode extends Node {

    private AddExpNode addExpNode;

    public ExpNode(AddExpNode addExpNode, int line) {
        super(line);
        this.addExpNode = addExpNode;
    }

    public DataType getDataType(SymbolTable currentSymbolTable) {
        return addExpNode.getDataType(currentSymbolTable);
    }

}
