package frontend.grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.grammaticalAnalysis.grammatical.StmtNode;
import myclasses.Pair;

public class StmtContinueNode extends StmtNode {

    public StmtContinueNode(int line) {
        super(line);
    }

    @Override
    public boolean checkErrorM(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.getLoop() == 0) {
            errors.add(new Pair<>(this.getLine(), SysYException.ExceptionCode.m));
            return true;
        } else {
            return false;
        }
    }
}
