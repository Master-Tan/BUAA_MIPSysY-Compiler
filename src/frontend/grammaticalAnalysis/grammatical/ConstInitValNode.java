package frontend.grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class ConstInitValNode extends Node {

    private ArrayList<ConstInitValNode> constInitValNodes;
    private ConstExpNode constExpNode;

    public ConstInitValNode(ArrayList<ConstInitValNode> constInitValNodes, int line) {
        super(line);
        this.constInitValNodes = constInitValNodes;
        this.constExpNode = null;
    }

    public ConstInitValNode(ConstExpNode constExpNode, int line) {
        super(line);
        this.constInitValNodes = null;
        this.constExpNode = constExpNode;
    }

    public ConstInitValNode(int line) {
        super(line);
        this.constInitValNodes = null;
        this.constExpNode = null;
    }

    public ArrayList<ConstInitValNode> getConstInitValNodes() {
        return constInitValNodes;
    }

    public ConstExpNode getConstExpNode() {
        return constExpNode;
    }
}
