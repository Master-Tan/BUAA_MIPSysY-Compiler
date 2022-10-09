package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

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

}
