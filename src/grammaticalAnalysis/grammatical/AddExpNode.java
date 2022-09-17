package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class AddExpNode {

    private AddExpNode addExpNode;
    private Word separator;
    private MulExpNode mulExpNode;

    public AddExpNode(MulExpNode mulExpNode) {
        this.addExpNode = null;
        this.separator = null;
        this.mulExpNode = mulExpNode;
    }

    public AddExpNode(AddExpNode addExpNode, Word separator, MulExpNode mulExpNode) {
        this.addExpNode = addExpNode;
        this.separator = separator;
        this.mulExpNode = mulExpNode;
    }

}
