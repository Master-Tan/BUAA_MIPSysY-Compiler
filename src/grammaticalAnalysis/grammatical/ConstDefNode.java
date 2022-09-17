package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class ConstDefNode {

    private Word ident;
    private ArrayList<ConstExpNode> constExpNodes;
    private ConstInitValNode constInitvalNode;

    public ConstDefNode(Word ident, ArrayList<ConstExpNode> constExpNodes, ConstInitValNode constInitvalNode) {
        this.ident = ident;
        this.constExpNodes = constExpNodes;
        this.constInitvalNode = constInitvalNode;
    }

}
