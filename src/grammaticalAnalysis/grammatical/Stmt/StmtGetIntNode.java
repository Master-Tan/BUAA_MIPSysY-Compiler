package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.LValNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtGetIntNode extends StmtNode {

    private LValNode lValNode;

    public StmtGetIntNode(LValNode lValNode, int line) {
        super(line);
        this.lValNode = lValNode;
    }
}
