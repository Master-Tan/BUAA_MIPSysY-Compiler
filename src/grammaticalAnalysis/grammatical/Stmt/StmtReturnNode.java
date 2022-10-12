package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.ExpNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtReturnNode extends StmtNode {

    private ExpNode expNode;

    public StmtReturnNode(ExpNode expNode, int line) {
        super(line);
        this.expNode = expNode;
    }

    public StmtReturnNode(int line) {
        super(line);
        this.expNode = null;
    }

    public ExpNode getExpNode() {
        return expNode;
    }
}
