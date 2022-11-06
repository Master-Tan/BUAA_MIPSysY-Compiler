package frontend.grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class ConstDeclNode extends Node {

    private ArrayList<ConstDefNode> constDefNodes;

    public ConstDeclNode(ArrayList<ConstDefNode> constDefNodes, int line) {
        super(line);
        this.constDefNodes = constDefNodes;
    }

    public ArrayList<ConstDefNode> getConstDefNodes() {
        return constDefNodes;
    }
}
