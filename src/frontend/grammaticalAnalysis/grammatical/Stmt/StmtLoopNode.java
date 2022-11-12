package frontend.grammaticalAnalysis.grammatical.Stmt;

import frontend.grammaticalAnalysis.grammatical.CondNode;
import frontend.grammaticalAnalysis.grammatical.StmtNode;

public class StmtLoopNode extends StmtNode {

    private CondNode condNode;
    private StmtNode stmtNode;

    public StmtLoopNode(CondNode condNode, StmtNode node, int line) {
        super(line);
        this.condNode = condNode;
        this.stmtNode = node;
    }

    public CondNode getCondNode() {
        return condNode;
    }

    public StmtNode getStmtNode() {
        return stmtNode;
    }
}
