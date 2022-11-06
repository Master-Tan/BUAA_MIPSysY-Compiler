package frontend.grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.SymbolTable;
import frontend.lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.util.ArrayList;

public class FuncFParamNode extends Node {

    private Word ident;
    private ArrayList<ConstExpNode> constExpNodes;

    public FuncFParamNode(Word ident, ArrayList<ConstExpNode> constExpNodes, int line) {
        super(line);
        this.ident = ident;
        this.constExpNodes = constExpNodes;
    }

    public FuncFParamNode(Word ident, int line) {
        super(line);
        this.ident = ident;
        this.constExpNodes = null;
    }

    public Word getIdent() {
        return ident;
    }

    public ArrayList<ConstExpNode> getConstExpNodes() {
        return constExpNodes;
    }

    @Override
    public String toString() {
        return "FuncFParamNode";
    }


    public boolean checkErrorB(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.isRedefine(ident)) {
            errors.add(new Pair<>(this.getLine(), SysYException.ExceptionCode.b));
            return true;
        } else {
            return false;
        }
    }

    public DataType getDataType() {
        if (constExpNodes == null) {
            return DataType.INT;
        } else if (constExpNodes.size() == 0) {
            return DataType.ARRAY1;
        } else {
            return DataType.ARRAY2;
        }
    }
}
