package frontend.grammaticalAnalysis.grammatical;

public class CondNode extends Node {

    private LOrExpNode lOrExpNode;

    public CondNode(LOrExpNode lOrExpNode, int line) {
        super(line);
        this.lOrExpNode = lOrExpNode;
    }

    public LOrExpNode getlOrExpNode() {
        return lOrExpNode;
    }
}
