package frontend.grammaticalAnalysis.grammatical.Stmt;

import frontend.grammaticalAnalysis.grammatical.CondNode;
import frontend.grammaticalAnalysis.grammatical.StmtNode;

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

    public CondNode getCondNode() {
        return condNode;
    }

    public StmtNode getStmtNodeIf() {
        return stmtNodeIf;
    }

    public StmtNode getStmtNodeElse() {
        return stmtNodeElse;
    }
}
