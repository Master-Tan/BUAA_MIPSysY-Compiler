package grammaticalAnalysis.grammatical;

public class BlockItemNode {

    private StmtNode stmtNode;
    private DeclNode declNode;

    public BlockItemNode(StmtNode stmtNode) {
        this.stmtNode = stmtNode;
        this.declNode = null;
    }

    public BlockItemNode(DeclNode declNode) {
        this.stmtNode = null;
        this.declNode = declNode;
    }

}
