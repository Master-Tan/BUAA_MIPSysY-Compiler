package grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class InitValNode {

    private ExpNode expNode;
    private ArrayList<InitValNode> initValNodes;

    public InitValNode() {
        this.expNode = null;
        this.initValNodes = null;
    }

    public InitValNode(ExpNode expNode) {
        this.expNode = expNode;
        this.initValNodes = null;
    }

    public InitValNode(ArrayList<InitValNode> nodes) {
        this.expNode = null;
        this.initValNodes = nodes;
    }
}
