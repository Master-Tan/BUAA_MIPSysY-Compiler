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
