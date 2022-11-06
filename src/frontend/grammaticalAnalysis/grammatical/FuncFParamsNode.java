package frontend.grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class FuncFParamsNode extends Node {

    private ArrayList<FuncFParamNode> funcFParamNodes;

    public FuncFParamsNode(ArrayList<FuncFParamNode> funcFParamNodes, int line) {
        super(line);
        this.funcFParamNodes = funcFParamNodes;
    }

    public ArrayList<FuncFParamNode> getFuncFParamNodes() {
        return funcFParamNodes;
    }
}
