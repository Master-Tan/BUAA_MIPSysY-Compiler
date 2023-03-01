package frontend.grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class InitValNode extends Node {

    private ExpNode expNode;
    private ArrayList<InitValNode> initValNodes;

    public InitValNode(int line) {
        super(line);
        this.expNode = null;
        this.initValNodes = null;
    }

    public InitValNode(ExpNode expNode, int line) {
        super(line);
        this.expNode = expNode;
        this.initValNodes = null;
    }

    public InitValNode(ArrayList<InitValNode> nodes, int line) {
        super(line);
        this.expNode = null;
        this.initValNodes = nodes;
    }

    public ExpNode getExpNode() {
        return expNode;
    }

    public ArrayList<InitValNode> getInitValNodes() {
        return initValNodes;
    }

}
