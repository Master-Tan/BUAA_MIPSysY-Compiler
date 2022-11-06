package frontend.grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class FuncRParamsNode extends Node {

    private ArrayList<ExpNode> expNodes;

    public FuncRParamsNode(ArrayList<ExpNode> expNodes, int line) {
        super(line);
        this.expNodes = expNodes;
    }

    public ArrayList<ExpNode> getExpNodes() {
        return expNodes;
    }
}
