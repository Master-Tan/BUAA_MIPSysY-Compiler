package grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.util.ArrayList;

public class ConstDefNode extends Node {

    private Word ident;
    private ArrayList<ConstExpNode> constExpNodes;
    private ConstInitValNode constInitvalNode;

    public ConstDefNode(Word ident, ArrayList<ConstExpNode> constExpNodes, ConstInitValNode constInitvalNode, int line) {
        super(line);
        this.ident = ident;
        this.constExpNodes = constExpNodes;
        this.constInitvalNode = constInitvalNode;
    }

    public Word getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return "ConstDefNode";
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
        if (constExpNodes == null) {
            return DataType.INT;
        } else if (constExpNodes.size() == 0) {
            return DataType.ARRAY1;
        } else {
            return DataType.ARRAY2;
        }
    }
}
