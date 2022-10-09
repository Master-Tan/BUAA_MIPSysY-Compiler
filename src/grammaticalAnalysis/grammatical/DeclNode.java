package grammaticalAnalysis.grammatical;

public class DeclNode extends Node {

    private ConstDeclNode constDeclNode;
    private VarDeclNode varDeclNode;

    public DeclNode(ConstDeclNode constDeclNode, int line) {
        super(line);
        this.constDeclNode = constDeclNode;
        this.varDeclNode = null;
    }

    public DeclNode(VarDeclNode varDeclNode, int line) {
        super(line);
        this.constDeclNode = null;
        this.varDeclNode = varDeclNode;
    }
}
