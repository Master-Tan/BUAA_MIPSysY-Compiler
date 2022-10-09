package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.ExpNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtExprNode extends StmtNode {

    private ExpNode expNode;

    public StmtExprNode(int line) {
        super(line);
    }

    public StmtExprNode(ExpNode expNode, int line) {
        super(line);
        this.expNode = expNode;
    }
}
