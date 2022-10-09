package grammaticalAnalysis.grammatical.Stmt;

import grammaticalAnalysis.grammatical.BlockNode;
import grammaticalAnalysis.grammatical.StmtNode;

public class StmtBlockNode extends StmtNode {

    private BlockNode blockNode;

    public StmtBlockNode(BlockNode blockNode, int line) {
        super(line);
        this.blockNode = blockNode;
    }

}
