package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class PrimaryExpNode {

    private ExpNode expNode;
    private LValNode lValNode;
    private NumberNode numberNode;

    public PrimaryExpNode(ExpNode expNode) {
        this.expNode = expNode;
        this.lValNode = null;
        this.numberNode = null;
    }

    public PrimaryExpNode(LValNode lValNode) {
        this.expNode = null;
        this.lValNode = lValNode;
        this.numberNode = null;
    }

    public PrimaryExpNode(NumberNode numberNode) {
        this.expNode = null;
        this.lValNode = null;
        this.numberNode = numberNode;
    }

}
