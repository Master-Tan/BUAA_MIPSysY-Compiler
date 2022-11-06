package frontend.grammaticalAnalysis.grammatical.Stmt;

import frontend.grammaticalAnalysis.grammatical.BlockNode;
import frontend.grammaticalAnalysis.grammatical.StmtNode;

public class StmtBlockNode extends StmtNode {

    private BlockNode blockNode;

    public StmtBlockNode(BlockNode blockNode, int line) {
        super(line);
        this.blockNode = blockNode;
    }

    public BlockNode getBlockNode() {
        return blockNode;
    }
}
