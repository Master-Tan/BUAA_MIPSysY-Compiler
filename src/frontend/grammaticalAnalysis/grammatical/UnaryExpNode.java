package frontend.grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.lexicalAnalysis.lexical.Word;
import myclasses.CategoryCodeEnum;
import myclasses.Pair;

public class UnaryExpNode extends Node {

    private PrimaryExpNode primaryExpNode;
    private Word ident;
    private FuncRParamsNode funcRParamsNode;
    private UnaryOpNode unaryOpNode;
    private UnaryExpNode unaryExpNode;

    public UnaryExpNode(PrimaryExpNode primaryExpNode, int line) {
        super(line);
        this.primaryExpNode = primaryExpNode;
        this.ident = null;
        this.funcRParamsNode = null;
        this.unaryOpNode = null;
        this.unaryExpNode = null;
    }

    public UnaryExpNode(Word ident, int line) {
        super(line);
        this.primaryExpNode = null;
        this.ident = ident;
        this.funcRParamsNode = null;
        this.unaryOpNode = null;
        this.unaryExpNode = null;
    }

    public UnaryExpNode(Word ident, FuncRParamsNode funcRParamsNode, int line) {
        super(line);
        this.primaryExpNode = null;
        this.ident = ident;
        this.funcRParamsNode = funcRParamsNode;
        this.unaryOpNode = null;
        this.unaryExpNode = null;
    }

    public UnaryExpNode(UnaryOpNode unaryOpNode, UnaryExpNode node, int line) {
        super(line);
        this.primaryExpNode = null;
        this.ident = null;
        this.funcRParamsNode = null;
        this.unaryOpNode = unaryOpNode;
        this.unaryExpNode = node;
    }

    public PrimaryExpNode getPrimaryExpNode() {
        return primaryExpNode;
    }

    public Word getIdent() {
        return ident;
    }

    public FuncRParamsNode getFuncRParamsNode() {
        return funcRParamsNode;
    }

    public UnaryOpNode getUnaryOpNode() {
        return unaryOpNode;
    }

    public UnaryExpNode getUnaryExpNode() {
        return unaryExpNode;
    }

    @Override
    public boolean checkErrorC(SymbolTable currentSymbolTable) {
        if (this.ident == null) {
            return false;
        } else {
            if (currentSymbolTable.isUndefine(ident)) {
                errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.c));
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean checkErrorD(SymbolTable currentSymbolTable) {
        if (this.ident == null) {
            return false;
        } else {
            if (currentSymbolTable.isParaNumberUnmatch(ident, funcRParamsNode)) {
                errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.d));
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean checkErrorE(SymbolTable currentSymbolTable) {
        if (this.funcRParamsNode == null) {
            return false;
        } else {
            if (currentSymbolTable.isParaTypeUnmatch(ident, funcRParamsNode)) {
                errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.e));
                return true;
            } else {
                return false;
            }
        }
    }

    public DataType getDataType(SymbolTable currentSymbolTable) {
        if (primaryExpNode != null) {
            return primaryExpNode.getDataType(currentSymbolTable);
        } else if (unaryExpNode != null) {
            return (unaryExpNode.getDataType(currentSymbolTable));
        } else {
            Word reserved = currentSymbolTable.getFunReturnType(ident);
            if (reserved.getCategoryCode() == CategoryCodeEnum.CategoryCode.VOIDTK) {
                return DataType.VOID;
            } else {
                return DataType.INT;
            }
        }
    }
}

