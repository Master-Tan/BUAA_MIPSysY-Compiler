package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class MulExpNode extends Node {

    private MulExpNode mulExpNode;
    private Word sepatator;
    private UnaryExpNode unaryExpNode;

    public MulExpNode(UnaryExpNode unaryExpNode, int line) {
        super(line);
        this.mulExpNode = null;
        this.sepatator = null;
        this.unaryExpNode = unaryExpNode;
    }

    public MulExpNode(MulExpNode mulExpNode, Word separator, UnaryExpNode unaryExpNode, int line) {
        super(line);
        this.mulExpNode = mulExpNode;
        this.sepatator = separator;
        this.unaryExpNode = unaryExpNode;
    }

}
