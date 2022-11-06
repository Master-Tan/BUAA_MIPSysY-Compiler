package frontend.grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.util.ArrayList;

public class VarDefNode extends Node {

    private Word ident;
    private ArrayList<ConstExpNode> constExpNodes;
    private InitValNode initValNode;

    public VarDefNode(Word ident, ArrayList<ConstExpNode> constExpNodes, int line) {
        super(line);
        this.ident = ident;
        this.constExpNodes = constExpNodes;
        this.initValNode = null;
    }

    public VarDefNode(Word ident, ArrayList<ConstExpNode> constExpNodes, InitValNode initValNode, int line) {
        super(line);
        this.ident = ident;
        this.constExpNodes = constExpNodes;
        this.initValNode = initValNode;
    }

    public Word getIdent() {
        return ident;
    }

    public ArrayList<ConstExpNode> getConstExpNodes() {
        return constExpNodes;
    }

    public InitValNode getInitValNode() {
        return initValNode;
    }

    @Override
    public String toString() {
        return "VarDefNode";
    }

    public boolean checkErrorB(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.isRedefine(ident)) {
            errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.b));
            return true;
        } else {
            return false;
        }
    }

    public DataType getDataType() {

        if (constExpNodes.size() == 0) {
            return DataType.INT;
        } else if (constExpNodes.size() == 1) {
            return DataType.ARRAY1;
        } else {
            return DataType.ARRAY2;
        }
    }
}