package lexicalanalysis.lexical;

import enums.CategoryCodeEnum.CategoryCode;

public class Word {

    private CategoryCode categoryCode;
    private String wordValue;
    private int lineNumber;

    public Word(CategoryCode categoryCode, String wordValue, int lineNumber) {
        this.categoryCode = categoryCode;
        this.wordValue = wordValue;
        this.lineNumber = lineNumber;
    }

    public CategoryCode getCategoryCode() {
        return categoryCode;
    }

    public String getWordValue() {
        return wordValue;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return "Word{" +
                "categoryCode=" + categoryCode +
                ", wordValue='" + wordValue + '\'' +
                ", lineNumber=" + lineNumber +
                '}';
    }
}
