package frontend.grammaticalAnalysis.grammatical;

import frontend.lexicalAnalysis.lexical.Word;

public class NumberNode extends Node {

    private Word intConst;

    public NumberNode(Word intConst, int line) {
        super(line);
        this.intConst = intConst;
    }

    public Word getIntConst() {
        return intConst;
    }
}
