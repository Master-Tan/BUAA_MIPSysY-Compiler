package grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import grammaticalAnalysis.grammatical.ExpNode;
import grammaticalAnalysis.grammatical.LValNode;
import grammaticalAnalysis.grammatical.StmtNode;
import myclasses.Pair;

public class StmtAssignNode extends StmtNode {

    private LValNode lValNode;
    private ExpNode expNode;

    public StmtAssignNode(LValNode lValNode, ExpNode expNode, int line) {
        super(line);
        this.lValNode = lValNode;
        this.expNode = expNode;
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
