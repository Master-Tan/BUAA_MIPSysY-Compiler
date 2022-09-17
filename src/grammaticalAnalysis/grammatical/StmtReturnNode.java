package grammaticalAnalysis.grammatical;

public class StmtReturnNode extends StmtNode {

    private ExpNode expNode;

    public StmtReturnNode(ExpNode expNode) {
        this.expNode = expNode;
    }

    public StmtReturnNode() {
        this.expNode = null;
    }

}
