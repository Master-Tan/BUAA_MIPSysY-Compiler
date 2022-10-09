package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

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

}
