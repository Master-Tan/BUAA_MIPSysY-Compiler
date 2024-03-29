package frontend.grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.lexicalAnalysis.lexical.Word;
import myclasses.Pair;

public class FuncDefNode extends Node {

    private FuncTypeNode funcTypeNode;
    private Word ident;
    private FuncFParamsNode funcFParamsNode;
    private BlockNode blockNode;

    // No Use
    public FuncDefNode(FuncTypeNode funcTypeNode, Word ident, int line) {
        super(line);
        this.funcTypeNode = funcTypeNode;
        this.ident = ident;
        this.funcFParamsNode = null;
        this.blockNode = null;
    }

    // No Use
    public FuncDefNode(FuncTypeNode funcTypeNode, Word ident, FuncFParamsNode funcFParamsNode, int line) {
        super(line);
        this.funcTypeNode = funcTypeNode;
        this.ident = ident;
        this.funcFParamsNode = funcFParamsNode;
        this.blockNode = null;
    }

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

    public FuncTypeNode getFuncTypeNode() {
        return funcTypeNode;
    }

    public BlockNode getBlockNode() {
        return blockNode;
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

    @Override
    public boolean checkErrorG(SymbolTable currentSymbolTable) {
        if (!funcTypeNode.getReserved().isVoid() && blockNode.getReturnType(currentSymbolTable) == null) {
            errors.add(new Pair<>(blockNode.getEndLine(), SysYException.ExceptionCode.g));
            return true;
        } else {
            return false;
        }
    }
}
