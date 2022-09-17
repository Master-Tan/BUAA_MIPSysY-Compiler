package grammaticalAnalysis.grammatical;

public class LAndExpNode {

    private LAndExpNode lAndExpNode;
    private EqExpNode eqExpNode;

    public LAndExpNode(EqExpNode eqExpNode) {
        this.lAndExpNode = null;
        this.eqExpNode = eqExpNode;
    }

    public LAndExpNode(LAndExpNode lAndExpNode, EqExpNode eqExpNode) {
        this.lAndExpNode = lAndExpNode;
        this.eqExpNode = eqExpNode;
    }

}
