package grammaticalAnalysis.grammatical;

public class StmtGetIntNode extends StmtNode {

    private LValNode lValNode;

    public StmtGetIntNode(LValNode lValNode) {
        this.lValNode = lValNode;
    }
}
