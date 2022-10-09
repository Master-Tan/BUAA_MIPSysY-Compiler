package lexicalAnalysis.lexical;

import myclasses.CategoryCodeEnum.CategoryCode;

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
        return categoryCode + " " + wordValue;
    }

    public boolean isIdent() {
        return this.categoryCode == CategoryCode.IDENFR;
    }

    public boolean isIntConst() {
        return this.categoryCode == CategoryCode.INTCON;
    }

    public boolean isFormatString() {
        return this.categoryCode == CategoryCode.STRCON;
    }

    public boolean isMain() {
        return this.categoryCode == CategoryCode.MAINTK;
    }

    public boolean isConst() {
        return this.categoryCode == CategoryCode.CONSTTK;
    }

    public boolean isInt() {
        return this.categoryCode == CategoryCode.INTTK;
    }

    public boolean isBreak() {
        return this.categoryCode == CategoryCode.BREAKTK;
    }

    public boolean isContinue() {
        return this.categoryCode == CategoryCode.CONTINUETK;
    }

    public boolean isIf() {
        return this.categoryCode == CategoryCode.IFTK;
    }

    public boolean isElse() {
        return this.categoryCode == CategoryCode.ELSETK;
    }

    public boolean isNot() {
        return this.categoryCode == CategoryCode.NOT;
    }

    public boolean isAnd() {
        return this.categoryCode == CategoryCode.AND;
    }

    public boolean isOr() {
        return this.categoryCode == CategoryCode.OR;
    }

    public boolean isWhile() {
        return this.categoryCode == CategoryCode.WHILETK;
    }

    public boolean isGetint() {
        return this.categoryCode == CategoryCode.GETINTTK;
    }

    public boolean isPrintf() {
        return this.categoryCode == CategoryCode.PRINTFTK;
    }

    public boolean isReturn() {
        return this.categoryCode == CategoryCode.RETURNTK;
    }

    public boolean isPlus() {
        return this.categoryCode == CategoryCode.PLUS;
    }

    public boolean isMinu() {
        return this.categoryCode == CategoryCode.MINU;
    }

    public boolean isVoid() {
        return this.categoryCode == CategoryCode.VOIDTK;
    }

    public boolean isMult() {
        return this.categoryCode == CategoryCode.MULT;
    }

    public boolean isDiv() {
        return this.categoryCode == CategoryCode.DIV;
    }

    public boolean isMod() {
        return this.categoryCode == CategoryCode.MOD;
    }

    public boolean isLss() {
        return this.categoryCode == CategoryCode.LSS;
    }

    public boolean isLeq() {
        return this.categoryCode == CategoryCode.LEQ;
    }

    public boolean isGre() {
        return this.categoryCode == CategoryCode.GRE;
    }

    public boolean isGeq() {
        return this.categoryCode == CategoryCode.GEQ;
    }

    public boolean isEql() {
        return this.categoryCode == CategoryCode.EQL;
    }

    public boolean isNeq() {
        return this.categoryCode == CategoryCode.NEQ;
    }

    public boolean isAssign() {
        return this.categoryCode == CategoryCode.ASSIGN;
    }

    public boolean isSemicn() {
        return this.categoryCode == CategoryCode.SEMICN;
    }

    public boolean isComma() {
        return this.categoryCode == CategoryCode.COMMA;
    }

    public boolean isLparent() {
        return this.categoryCode == CategoryCode.LPARENT;
    }

    public boolean isRParent() {
        return this.categoryCode == CategoryCode.RPARENT;
    }

    public boolean isLbrack() {
        return this.categoryCode == CategoryCode.LBRACK;
    }

    public boolean isRbrack() {
        return this.categoryCode == CategoryCode.RBRACK;
    }

    public boolean isLbrace() {
        return this.categoryCode == CategoryCode.LBRACE;
    }

    public boolean isRbrace() {
        return this.categoryCode == CategoryCode.RBRACE;
    }

}
