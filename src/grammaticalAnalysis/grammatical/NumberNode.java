package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class NumberNode extends Node {

    private Word intConst;

    public NumberNode(Word intConst, int line) {
        super(line);
        this.intConst = intConst;
    }

}
