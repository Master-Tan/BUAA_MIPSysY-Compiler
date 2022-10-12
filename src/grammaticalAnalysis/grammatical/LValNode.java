package grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.util.ArrayList;

public class LValNode extends Node {

    private Word ident;
    private ArrayList<ExpNode> expNodes;

    public LValNode(Word ident, ArrayList<ExpNode> expNodes, int line) {
        super(line);
        this.ident = ident;
        this.expNodes = expNodes;
    }

    public Word getIdent() {
        return ident;
    }

    public DataType getDataType(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.getVarType(ident) == null) {
            return null;
        }
        if (currentSymbolTable.getVarType(ident) == DataType.INT) {
            return DataType.INT;
        } else if (currentSymbolTable.getVarType(ident) == DataType.ARRAY1) {
            if (expNodes.size() == 0) {
                return DataType.ARRAY1;
            } else {
                return DataType.INT;
            }
        } else {
            if (expNodes.size() == 0) {
                return DataType.ARRAY2;
            } else if (expNodes.size() == 1) {
                return DataType.ARRAY1;
            } else {
                return DataType.INT;
            }
        }
    }

    @Override
    public boolean checkErrorC(SymbolTable currentSymbolTable) {
        if (currentSymbolTable.isUndefine(ident)) {
            errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.c));
            return true;
        } else {
            return false;
        }
    }
}
