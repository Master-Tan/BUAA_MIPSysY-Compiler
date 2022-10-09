package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class PrimaryExpNode extends Node {

    private ExpNode expNode;
    private LValNode lValNode;
    private NumberNode numberNode;

    public PrimaryExpNode(ExpNode expNode, int line) {
        super(line);
        this.expNode = expNode;
        this.lValNode = null;
        this.numberNode = null;
    }

    public PrimaryExpNode(LValNode lValNode, int line) {
        super(line);
        this.expNode = null;
        this.lValNode = lValNode;
        this.numberNode = null;
    }

    public PrimaryExpNode(NumberNode numberNode, int line) {
        super(line);
        this.expNode = null;
        this.lValNode = null;
        this.numberNode = numberNode;
    }

}
