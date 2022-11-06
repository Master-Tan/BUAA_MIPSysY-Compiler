package frontend.grammaticalAnalysis.grammatical.Stmt;

import frontend.grammaticalAnalysis.grammatical.ExpNode;
import frontend.grammaticalAnalysis.grammatical.StmtNode;

public class StmtExprNode extends StmtNode {

    private ExpNode expNode;

    public StmtExprNode(int line) {
        super(line);
    }

    public StmtExprNode(ExpNode expNode, int line) {
        super(line);
        this.expNode = expNode;
    }

    public ExpNode getExpNode() {
        return expNode;
    }
}
