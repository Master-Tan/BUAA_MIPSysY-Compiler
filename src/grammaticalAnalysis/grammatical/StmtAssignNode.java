package grammaticalAnalysis.grammatical;

public class StmtAssignNode extends StmtNode {

    private LValNode lValNode;
    private ExpNode expNode;

    public StmtAssignNode(LValNode lValNode, ExpNode expNode) {
        this.lValNode = lValNode;
        this.expNode = expNode;
    }
}
