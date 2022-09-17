package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class FuncFParamNode {

    private Word ident;
    private ArrayList<ConstExpNode> constExpNodes;

    public FuncFParamNode(Word ident, ArrayList<ConstExpNode> constExpNodes) {
        this.ident = ident;
        this.constExpNodes = constExpNodes;
    }

    public FuncFParamNode(Word ident) {
        this.ident = ident;
        this.constExpNodes = null;
    }

}
