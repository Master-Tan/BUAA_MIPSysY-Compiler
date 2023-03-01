package frontend.lexicalAnalysis.lexical;

import myclasses.CategoryCodeEnum.CategoryCode;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Separator extends Word {

    public static final String PATTERN;
    public static final HashMap<String, CategoryCode> separatorWords = new HashMap<>();

    static {
        separatorWords.put("!", CategoryCode.NOT);
        separatorWords.put("&&", CategoryCode.AND);
        separatorWords.put("||", CategoryCode.OR);
        separatorWords.put("+", CategoryCode.PLUS);
        separatorWords.put("-", CategoryCode.MINU);
        separatorWords.put("*", CategoryCode.MULT);
        separatorWords.put("/", CategoryCode.DIV);
        separatorWords.put("%", CategoryCode.MOD);
        separatorWords.put("<", CategoryCode.LSS);
        separatorWords.put("<=", CategoryCode.LEQ);
        separatorWords.put(">", CategoryCode.GRE);
        separatorWords.put(">=", CategoryCode.GEQ);
        separatorWords.put("==", CategoryCode.EQL);
        separatorWords.put("!=", CategoryCode.NEQ);
        separatorWords.put("=", CategoryCode.ASSIGN);
        separatorWords.put(";", CategoryCode.SEMICN);
        separatorWords.put(",", CategoryCode.COMMA);
        separatorWords.put("(", CategoryCode.LPARENT);
        separatorWords.put(")", CategoryCode.RPARENT);
        separatorWords.put("[", CategoryCode.LBRACK);
        separatorWords.put("]", CategoryCode.RBRACK);
        separatorWords.put("{", CategoryCode.LBRACE);
        separatorWords.put("}", CategoryCode.RBRACE);
        separatorWords.put("bitand", CategoryCode.BITAND);
        StringBuffer pattern = new StringBuffer();
        for (String separatorWord : separatorWords.keySet()) {

            if (separatorWord == "||") {
                pattern.insert(0, "(\\|\\|)|");
                continue;
            }

            if (separatorWord.length() == 2) {
                pattern.insert(0, "(" + separatorWord + ")" + "|");
            } else {
                Pattern escapePattern =
                        Pattern.compile("(\\+)|(\\*)|(\\()|(\\))|(\\[)|(\\])|(\\{)|(\\})|(\\/)");
                Matcher escapeMatcher = escapePattern.matcher(separatorWord);
                if (escapeMatcher.find()) {
                    pattern.append("(" + "\\" + separatorWord + ")" + "|");
                } else {
                    pattern.append("(" + separatorWord + ")" + "|");
                }
            }

        }
        pattern.deleteCharAt(pattern.length() - 1);
        PATTERN = pattern.toString();
    }

    public Separator(CategoryCode categoryCode, String wordValue, int lineNumber) {
        super(categoryCode, wordValue, lineNumber);
    }

}
