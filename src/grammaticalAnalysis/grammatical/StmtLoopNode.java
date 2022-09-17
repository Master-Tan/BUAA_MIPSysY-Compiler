package grammaticalAnalysis.grammatical;

public class StmtLoopNode extends StmtNode {

    private CondNode condNode;
    private StmtNode stmtNode;

    public StmtLoopNode(CondNode condNode, StmtNode node) {
        this.condNode = condNode;
        this.stmtNode = node;
    }

}
