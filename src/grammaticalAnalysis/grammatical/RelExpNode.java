package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class RelExpNode extends Node {

    private RelExpNode relExpNode;
    private Word separator;
    private AddExpNode addExpNode;

    public RelExpNode(AddExpNode addExpNode, int line) {
        super(line);
        this.relExpNode = null;
        this.separator = null;
        this.addExpNode = addExpNode;
    }

    public RelExpNode(RelExpNode relExpNode, Word separator, AddExpNode addExpNode, int line) {
        super(line);
        this.relExpNode = relExpNode;
        this.separator = separator;
        this.addExpNode = addExpNode;
    }

}
