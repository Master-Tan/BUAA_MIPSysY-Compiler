package grammaticalAnalysis.grammatical;

public class StmtExprNode extends StmtNode {

    private ExpNode expNode;

    public StmtExprNode() {
    }

    public StmtExprNode(ExpNode expNode) {
        this.expNode = expNode;
    }
}
