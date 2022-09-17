package grammaticalAnalysis.grammatical;

public class StmtBranchNode extends StmtNode {

    private CondNode condNode;
    private StmtNode stmtNodeIf;
    private StmtNode stmtNodeElse;

    public StmtBranchNode(CondNode condNode, StmtNode node, StmtNode node2) {
        this.condNode = condNode;
        this.stmtNodeIf = node;
        this.stmtNodeElse = node2;
    }

    public StmtBranchNode(CondNode condNode, StmtNode node) {
        this.condNode = condNode;
        this.stmtNodeIf = node;
        this.stmtNodeElse = null;
    }

}
