package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.ExpNode;
import grammaticalAnalysis.grammatical.LValNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtAssignNode extends StmtNode {

    private LValNode lValNode;
    private ExpNode expNode;

    public StmtAssignNode(LValNode lValNode, ExpNode expNode, int line) {
        super(line);
        this.lValNode = lValNode;
        this.expNode = expNode;
    }
}
