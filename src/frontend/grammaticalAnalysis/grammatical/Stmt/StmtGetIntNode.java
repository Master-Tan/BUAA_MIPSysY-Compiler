package frontend.grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.grammaticalAnalysis.grammatical.LValNode;
import frontend.grammaticalAnalysis.grammatical.StmtNode;
import myclasses.Pair;

public class StmtGetIntNode extends StmtNode {

    private LValNode lValNode;

    public StmtGetIntNode(LValNode lValNode, int line) {
        super(line);
        this.lValNode = lValNode;
    }

    public LValNode getlValNode() {
        return lValNode;
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
