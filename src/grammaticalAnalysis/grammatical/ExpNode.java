package grammaticalAnalysis.grammatical;

public class ExpNode extends Node {

    private AddExpNode addExpNode;

    public ExpNode(AddExpNode addExpNode, int line) {
        super(line);
        this.addExpNode = addExpNode;
    }

}
