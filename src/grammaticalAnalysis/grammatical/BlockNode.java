package grammaticalAnalysis.grammatical;

import java.util.ArrayList;

public class BlockNode extends Node {

    private ArrayList<BlockItemNode> blockItemNode;

    public BlockNode(ArrayList<BlockItemNode> blockItemNode, int line) {
        super(line);
        this.blockItemNode = blockItemNode;
    }

}
