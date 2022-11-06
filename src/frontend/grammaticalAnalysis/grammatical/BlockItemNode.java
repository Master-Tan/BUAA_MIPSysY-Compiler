package frontend.grammaticalAnalysis.grammatical;

public class BlockItemNode extends Node {

    private StmtNode stmtNode;
    private DeclNode declNode;

    public BlockItemNode(StmtNode stmtNode, int line) {
        super(line);
        this.stmtNode = stmtNode;
        this.declNode = null;
    }

    public BlockItemNode(DeclNode declNode, int line) {
        super(line);
        this.stmtNode = null;
        this.declNode = declNode;
    }

    public StmtNode getStmtNode() {
        return stmtNode;
    }

    public DeclNode getDeclNode() {
        return declNode;
    }
}
