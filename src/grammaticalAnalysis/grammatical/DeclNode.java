package grammaticalAnalysis.grammatical;

public class DeclNode extends Node {

    private ConstDeclNode constDeclNode;
    private VarDeclNode varDeclNode;

    public DeclNode(ConstDeclNode constDeclNode) {
        this.constDeclNode = constDeclNode;
        this.varDeclNode = null;
    }

    public DeclNode(VarDeclNode varDeclNode) {
        this.constDeclNode = null;
        this.varDeclNode = varDeclNode;
    }
}
