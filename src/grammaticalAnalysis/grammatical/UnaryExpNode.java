package grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import lexicalAnalysis.lexical.Word;
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
        if (this.funcRParamsNode == null) {
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
                errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.d));
                return true;
            } else {
                return false;
            }
        }
    }
}

