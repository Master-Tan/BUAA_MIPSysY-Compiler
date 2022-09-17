package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class UnaryExpNode {

    private PrimaryExpNode primaryExpNode;
    private Word ident;
    private FuncRParamsNode funcRParamsNode;
    private UnaryOpNode unaryOpNode;
    private UnaryExpNode unaryExpNode;

    public UnaryExpNode(PrimaryExpNode primaryExpNode) {
        this.primaryExpNode = primaryExpNode;
        this.ident = null;
        this.funcRParamsNode = null;
        this.unaryOpNode = null;
        this.unaryExpNode = null;
    }

    public UnaryExpNode(Word ident) {
        this.primaryExpNode = null;
        this.ident = ident;
        this.funcRParamsNode = null;
        this.unaryOpNode = null;
        this.unaryExpNode = null;
    }

    public UnaryExpNode(Word ident, FuncRParamsNode funcRParamsNode) {
        this.primaryExpNode = null;
        this.ident = ident;
        this.funcRParamsNode = funcRParamsNode;
        this.unaryOpNode = null;
        this.unaryExpNode = null;
    }

    public UnaryExpNode(UnaryOpNode unaryOpNode, UnaryExpNode node) {
        this.primaryExpNode = null;
        this.ident = null;
        this.funcRParamsNode = null;
        this.unaryOpNode = unaryOpNode;
        this.unaryExpNode = node;
    }

}
