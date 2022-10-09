package grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

public class FuncDefNode extends Node {

    private FuncTypeNode funcTypeNode;
    private Word ident;
    private FuncFParamsNode funcFParamsNode;
    private BlockNode blockNode;

    public FuncDefNode(FuncTypeNode funcTypeNode, Word ident, FuncFParamsNode funcFParamsNode, BlockNode blockNode, int line) {
        super(line);
        this.funcTypeNode = funcTypeNode;
        this.ident = ident;
        this.funcFParamsNode = funcFParamsNode;
        this.blockNode = blockNode;
    }

    public FuncDefNode(FuncTypeNode funcTypeNode, Word ident, BlockNode blockNode, int line) {
        super(line);
        this.funcTypeNode = funcTypeNode;
        this.ident = ident;
        this.funcFParamsNode = null;
        this.blockNode = blockNode;
    }

    public FuncDefNode(int line) {
        super(line);
    }

    public Word getIdent() {
        return ident;
    }

    public FuncFParamsNode getFuncFParamsNode() {
        return funcFParamsNode;
    }

    @Override
    public String toString() {
        return "FuncDefNode";
    }

    public boolean checkErrorB(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.isRedefine(ident)) {
            errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.b));
            return true;
        } else {
            return false;
        }
    }
}
