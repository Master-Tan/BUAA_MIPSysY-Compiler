package frontend.grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class VarDeclNode extends Node {

    private ArrayList<VarDefNode> varDefNodes;

    public VarDeclNode(ArrayList<VarDefNode> varDefNodes, int line) {
        super(line);
        this.varDefNodes = varDefNodes;
    }

    public ArrayList<VarDefNode> getVarDefNodes() {
        return varDefNodes;
    }
}
