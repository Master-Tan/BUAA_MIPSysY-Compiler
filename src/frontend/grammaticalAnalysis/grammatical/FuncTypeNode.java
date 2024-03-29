package frontend.grammaticalAnalysis.grammatical;

import frontend.lexicalAnalysis.lexical.Word;

public class FuncTypeNode extends Node {

    private Word reserved;

    public FuncTypeNode(Word reserved, int line) {
        super(line);
        this.reserved = reserved;
    }

    public Word getReserved() {
        return reserved;
    }
}
