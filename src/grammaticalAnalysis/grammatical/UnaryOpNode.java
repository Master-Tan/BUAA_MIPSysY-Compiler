package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class UnaryOpNode extends Node {

    private Word separator;

    public UnaryOpNode(Word separator, int line) {
        super(line);
        this.separator = separator;
    }
}
