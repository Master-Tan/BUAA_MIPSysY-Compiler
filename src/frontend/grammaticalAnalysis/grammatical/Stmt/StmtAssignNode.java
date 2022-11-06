package frontend.grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.grammaticalAnalysis.grammatical.ExpNode;
import frontend.grammaticalAnalysis.grammatical.LValNode;
import frontend.grammaticalAnalysis.grammatical.StmtNode;
import myclasses.Pair;

public class StmtAssignNode extends StmtNode {

    private LValNode lValNode;
    private ExpNode expNode;

    public StmtAssignNode(LValNode lValNode, ExpNode expNode, int line) {
        super(line);
        this.lValNode = lValNode;
        this.expNode = expNode;
    }

    public LValNode getlValNode() {
        return lValNode;
    }

    public ExpNode getExpNode() {
        return expNode;
    }

    @Override
    public boolean checkErrorH(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.isConst(lValNode.getIdent())) {
            errors.add(new Pair<>(lValNode.getLine(), SysYException.ExceptionCode.h));
            return true;
        } else {
            return false;
        }
    }
}
