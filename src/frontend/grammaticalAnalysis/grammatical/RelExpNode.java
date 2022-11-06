package frontend.grammaticalAnalysis.grammatical;

import frontend.lexicalAnalysis.lexical.Word;

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

    public RelExpNode getRelExpNode() {
        return relExpNode;
    }

    public Word getSeparator() {
        return separator;
    }

    public AddExpNode getAddExpNode() {
        return addExpNode;
    }
}
