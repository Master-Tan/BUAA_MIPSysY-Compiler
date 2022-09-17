package grammaticalAnalysis.grammatical;

public class LOrExpNode {

    private LOrExpNode lOrExpNode;
    private LAndExpNode lAndExpNode;

    public LOrExpNode(LAndExpNode lAndExpNode) {
        this.lOrExpNode = null;
        this.lAndExpNode = lAndExpNode;
    }

    public LOrExpNode(LOrExpNode lOrExpNode, LAndExpNode lAndExpNode) {
        this.lOrExpNode = lOrExpNode;
        this.lAndExpNode = lAndExpNode;
    }

}
