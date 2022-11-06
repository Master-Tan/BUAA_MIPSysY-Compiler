package frontend.grammaticalAnalysis.grammatical;

import frontend.lexicalAnalysis.lexical.Word;

public class EqExpNode extends Node {

    private EqExpNode eqExpNode;
    private Word separator;
    private RelExpNode relExpNode;

    public EqExpNode(EqExpNode eqExpNode, Word separator, RelExpNode relExpNode, int line) {
        super(line);
        this.eqExpNode = eqExpNode;
        this.separator = separator;
        this.relExpNode = relExpNode;
    }

    public EqExpNode(RelExpNode relExpNode, int line) {
        super(line);
        this.eqExpNode = null;
        this.separator = null;
        this.relExpNode = relExpNode;
    }

    public EqExpNode getEqExpNode() {
        return eqExpNode;
    }

    public Word getSeparator() {
        return separator;
    }

    public RelExpNode getRelExpNode() {
        return relExpNode;
    }
}
