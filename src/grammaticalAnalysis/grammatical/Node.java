package grammaticalAnalysis.grammatical;

import exceptions.SysYException;
import grammaticalAnalysis.SymbolTable;
import myclasses.Pair;

import java.util.ArrayList;

public class Node {

    private int line;

    public static final ArrayList<Pair<Integer, SysYException.ExceptionCode>> errors = new ArrayList<>();

    public Node(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean checkErrorA() {
        return false;
    }

    public boolean checkErrorB(SymbolTable symbolTable) {
        return false;
    }

    public boolean checkErrorC(SymbolTable symbolTable) {
        return false;
    }

    public boolean checkErrorD(SymbolTable symbolTable) {
        return false;
    }

    public boolean checkErrorE(SymbolTable symbolTable) {
        return false;
    }

    public boolean checkErrorL() {
        return false;
    }
}
