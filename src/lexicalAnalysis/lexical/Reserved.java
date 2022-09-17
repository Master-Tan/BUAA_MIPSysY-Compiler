package lexicalAnalysis.lexical;

import enums.CategoryCodeEnum.CategoryCode;

import java.util.HashMap;

public class Reserved extends Word {

    public static final String PATTERN;
    public static final HashMap<String, CategoryCode> reservedWords = new HashMap<>();

    static {
        reservedWords.put("main", CategoryCode.MAINTK);
        reservedWords.put("const", CategoryCode.CONSTTK);
        reservedWords.put("int", CategoryCode.INTTK);
        reservedWords.put("break", CategoryCode.BREAKTK);
        reservedWords.put("continue", CategoryCode.CONTINUETK);
        reservedWords.put("if", CategoryCode.IFTK);
        reservedWords.put("else", CategoryCode.ELSETK);
        reservedWords.put("while", CategoryCode.WHILETK);
        reservedWords.put("getint", CategoryCode.GETINTTK);
        reservedWords.put("printf", CategoryCode.PRINTFTK);
        reservedWords.put("return", CategoryCode.RETURNTK);
        reservedWords.put("void", CategoryCode.VOIDTK);
        StringBuffer pattern = new StringBuffer();
        for (String reservedWord : reservedWords.keySet()) {
            pattern.append("(" + reservedWord + ")" + "|");
        }
        pattern.deleteCharAt(pattern.length() - 1);
        PATTERN = pattern.toString();
    }

    public Reserved(CategoryCode categoryCode, String wordValue, int lineNumber) {
        super(categoryCode, wordValue, lineNumber);
    }

}
