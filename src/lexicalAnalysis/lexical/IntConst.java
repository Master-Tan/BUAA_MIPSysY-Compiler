package lexicalAnalysis.lexical;

import myclasses.CategoryCodeEnum;

public class IntConst extends Word {

    public static final String PATTERN = "(0)|([1-9][0-9]*)";

    public IntConst(CategoryCodeEnum.CategoryCode categoryCode, String wordValue, int lineNumber) {
        super(categoryCode, wordValue, lineNumber);
    }

}
