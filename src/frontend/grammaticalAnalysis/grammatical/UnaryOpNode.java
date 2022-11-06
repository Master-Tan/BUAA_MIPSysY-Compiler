package frontend.grammaticalAnalysis.grammatical;

import frontend.lexicalAnalysis.lexical.Word;

public class UnaryOpNode extends Node {

    private Word separator;

    public UnaryOpNode(Word separator, int line) {
        super(line);
        this.separator = separator;
    }

    public Word getSeparator() {
        return separator;
    }
}
