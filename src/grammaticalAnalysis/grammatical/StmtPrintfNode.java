package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class StmtPrintfNode extends StmtNode {

    private Word formatString;
    private ArrayList<ExpNode> expNodes;

    public StmtPrintfNode(Word formatString, ArrayList<ExpNode> expNodes) {
        this.formatString = formatString;
        this.expNodes = expNodes;
    }

}
