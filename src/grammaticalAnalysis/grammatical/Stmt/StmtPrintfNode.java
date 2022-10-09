package grammaticalAnalysis.grammatical.Stmt;

import exceptions.SysYException;
import grammaticalAnalysis.grammatical.ExpNode;
import grammaticalAnalysis.grammatical.StmtNode;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.util.ArrayList;

public class StmtPrintfNode extends StmtNode {

    private Word formatString;
    private ArrayList<ExpNode> expNodes;

    public StmtPrintfNode(Word formatString, ArrayList<ExpNode> expNodes, int line) {
        super(line);
        this.formatString = formatString;
        this.expNodes = expNodes;

        if (checkErrorA()) {
            errors.add(new Pair(formatString.getLineNumber(), SysYException.ExceptionCode.a));
        }
        if (checkErrorL()) {
            errors.add(new Pair(this.getLine(), SysYException.ExceptionCode.l));
        }

    }

    public boolean checkErrorL() {
        String str = this.formatString.getWordValue();
        if (str.split("%d").length - 1 != expNodes.size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkErrorA() {
        String str = this.formatString.getWordValue();
        str = str.substring(1, str.length() - 1);
        boolean flag = true;
        for (int i = 0; i < str.length(); i++) {
            ArrayList<Integer> legalWord = new ArrayList<Integer>() {{
                add(32);
                add(33);
                for (int i = 40; i <= 126; i++) {
                    add(i);
                }
                add(37);
            }};
            if (!legalWord.contains((int) str.charAt(i))) {
                flag = false;
            }
            if (str.charAt(i) == '\\') {
                if (i == str.length() - 1) {
                    flag = false;
                } else {
                    if (str.charAt(i + 1) != 'n') {
                        flag = false;
                    }
                }
            }
            if (str.charAt(i) == '%') {
                if (i == str.length() - 1) {
                    flag = false;
                } else {
                    if (str.charAt(i + 1) != 'd') {
                        flag = false;
                    }
                }
            }
        }
        return !flag;
    }

}
