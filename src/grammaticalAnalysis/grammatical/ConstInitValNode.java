package grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class ConstInitValNode {

    private ArrayList<ConstInitValNode> constInitValNodes;
    private ConstExpNode constExpNode;

    public ConstInitValNode(ArrayList<ConstInitValNode> constInitValNodes) {
        this.constInitValNodes = constInitValNodes;
        this.constExpNode = null;
    }

    public ConstInitValNode(ConstExpNode constExpNode) {
        this.constInitValNodes = null;
        this.constExpNode = constExpNode;
    }
}
