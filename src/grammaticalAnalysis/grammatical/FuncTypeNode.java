package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class FuncTypeNode extends Node {

    private Word reserved;

    public FuncTypeNode(Word reserved, int line) {
        super(line);
        this.reserved = reserved;
    }

}
