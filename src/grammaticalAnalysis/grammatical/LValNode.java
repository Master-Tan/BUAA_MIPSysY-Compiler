package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class LValNode {

    private Word ident;
    private ArrayList<ExpNode> expNodes;

    public LValNode(Word ident, ArrayList<ExpNode> expNodes) {
        this.ident = ident;
        this.expNodes = expNodes;
    }

}
