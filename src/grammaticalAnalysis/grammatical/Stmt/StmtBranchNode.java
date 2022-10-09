package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.CondNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtBranchNode extends StmtNode {

    private CondNode condNode;
    private StmtNode stmtNodeIf;
    private StmtNode stmtNodeElse;

    public StmtBranchNode(CondNode condNode, StmtNode node, StmtNode node2, int line) {
        super(line);
        this.condNode = condNode;
        this.stmtNodeIf = node;
        this.stmtNodeElse = node2;
    }

    public StmtBranchNode(CondNode condNode, StmtNode node, int line) {
        super(line);
        this.condNode = condNode;
        this.stmtNodeIf = node;
        this.stmtNodeElse = null;
    }

}