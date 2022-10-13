package grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

public class MainFuncDefNode extends FuncDefNode {

    private Word ident;

    private BlockNode blockNode;

    public MainFuncDefNode(Word ident, BlockNode blockNode, int line) {
        super(line);
        this.ident = ident;
        this.blockNode = blockNode;
    }

    public MainFuncDefNode(Word ident, int line) {
        super(line);
        this.ident = ident;
        this.blockNode = null;
    }

    public Word getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return "MainFuncDefNode";
    }

    public boolean checkErrorB(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.isRedefine(ident)) {
            errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.b));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkErrorG(SymbolTable currentSymbolTable) {
        if (blockNode.getReturnType(currentSymbolTable) == null) {
            errors.add(new Pair<>(blockNode.getEndLine(), SysYException.ExceptionCode.g));
            return true;
        } else {
            return false;
        }
    }
}
