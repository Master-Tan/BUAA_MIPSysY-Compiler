package lexicalanalysis.lexical;

import enums.CategoryCodeEnum;

public class Ident extends Word {

    public static final String PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*";

    public Ident(CategoryCodeEnum.CategoryCode categoryCode, String wordValue, int lineNumber) {
        super(categoryCode, wordValue, lineNumber);
    }

}
