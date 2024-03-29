package frontend.grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.grammaticalAnalysis.grammatical.StmtNode;
import myclasses.Pair;

public class StmtBreakNode extends StmtNode {

    public StmtBreakNode(int line) {
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
