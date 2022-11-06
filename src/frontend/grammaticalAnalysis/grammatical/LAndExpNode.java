package frontend.grammaticalAnalysis.grammatical;

public class LAndExpNode extends Node {

    private LAndExpNode lAndExpNode;
    private EqExpNode eqExpNode;

    public LAndExpNode(EqExpNode eqExpNode, int line) {
        super(line);
        this.lAndExpNode = null;
        this.eqExpNode = eqExpNode;
    }

    public LAndExpNode(LAndExpNode lAndExpNode, EqExpNode eqExpNode, int line) {
        super(line);
        this.lAndExpNode = lAndExpNode;
        this.eqExpNode = eqExpNode;
    }

    public LAndExpNode getlAndExpNode() {
        return lAndExpNode;
    }

    public EqExpNode getEqExpNode() {
        return eqExpNode;
    }
}
