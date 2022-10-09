package grammaticalAnalysis.grammatical;

public class ConstExpNode extends Node {

    private AddExpNode addExpNode;

    public ConstExpNode(AddExpNode addExpNode, int line) {
        super(line);
        this.addExpNode = addExpNode;
    }

}
