package grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import grammaticalAnalysis.grammatical.LValNode;
import grammaticalAnalysis.grammatical.StmtNode;
import myclasses.Pair;

public class StmtGetIntNode extends StmtNode {

    private LValNode lValNode;

    public StmtGetIntNode(LValNode lValNode, int line) {
        super(line);
        this.lValNode = lValNode;
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
