package frontend.lexicalAnalysis.lexical;

import myclasses.CategoryCodeEnum.CategoryCode;

public class FormatString extends Word {

    public static final String PATTERN = "[\"].*?[\"]";

    public FormatString(CategoryCode categoryCode, String wordValue, int lineNumber) {
        super(categoryCode, wordValue, lineNumber);
    }

}
