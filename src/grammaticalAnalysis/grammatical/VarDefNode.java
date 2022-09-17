package grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class VarDefNode {

    private ArrayList<ConstExpNode> constExpNodes;
    private InitValNode initValNode;

    public VarDefNode(ArrayList<ConstExpNode> constExpNodes) {
        this.constExpNodes = constExpNodes;
        this.initValNode = null;
    }

    public VarDefNode(ArrayList<ConstExpNode> constExpNodes, InitValNode initValNode) {
        this.constExpNodes = constExpNodes;
        this.initValNode = initValNode;
    }

}
