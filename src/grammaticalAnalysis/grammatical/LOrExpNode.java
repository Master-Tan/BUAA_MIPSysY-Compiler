package grammaticalAnalysis.grammatical;

public class LOrExpNode extends Node {

    private LOrExpNode lOrExpNode;
    private LAndExpNode lAndExpNode;

    public LOrExpNode(LAndExpNode lAndExpNode, int line) {
        super(line);
        this.lOrExpNode = null;
        this.lAndExpNode = lAndExpNode;
    }

    public LOrExpNode(LOrExpNode lOrExpNode, LAndExpNode lAndExpNode, int line) {
        super(line);
        this.lOrExpNode = lOrExpNode;
        this.lAndExpNode = lAndExpNode;
    }

}
