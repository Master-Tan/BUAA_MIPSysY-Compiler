package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.CondNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtLoopNode extends StmtNode {

    private CondNode condNode;
    private StmtNode stmtNode;

    public StmtLoopNode(CondNode condNode, StmtNode node, int line) {
        super(line);
        this.condNode = condNode;
        this.stmtNode = node;
    }

}
