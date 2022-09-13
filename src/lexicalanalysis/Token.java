package lexicalanalysis;

import enums.CategoryCodeEnum.CategoryCode;
import lexicalanalysis.lexical.FormatString;
import lexicalanalysis.lexical.Ident;
import lexicalanalysis.lexical.IntConst;
import lexicalanalysis.lexical.Reserved;
import lexicalanalysis.lexical.Separator;
import lexicalanalysis.lexical.Word;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {

    private StringBuffer codeBuffer;  // 原本的代码

    private int lineNumber;  // 记录行号
    private int index;  // 记录当前位置
    private int codeLength;  // 代码长度

    public Token(StringBuffer codeBuffer) {

        this.codeBuffer = codeBuffer;
        this.lineNumber = 1;
        this.index = 0;
        this.codeLength = this.codeBuffer.length();

    }

    public Word nextWord() {

        while (stripWord()) {
        }
        ;

        if (index >= codeLength) {
            return null;
        }

        Pattern identPattern = Pattern.compile(Ident.PATTERN);
        Matcher identMatcher = identPattern.matcher(codeBuffer);
        if (identMatcher.find(index) && identMatcher.start() == index) {
            Pattern reservedPattern = Pattern.compile(Reserved.PATTERN);
            Matcher reservedMatcher = reservedPattern.matcher(codeBuffer);
            if (reservedMatcher.find(index) && reservedMatcher.start() == index
                    && reservedMatcher.end() == identMatcher.end()) {
                String values = reservedMatcher.group(0);
                Word word = new Reserved(Reserved.reservedWords.get(values), values, lineNumber);
                index = reservedMatcher.end();
                return word;
            }
            String values = identMatcher.group(0);
            Word word = new Ident(CategoryCode.IDENFR, values, lineNumber);
            index = identMatcher.end();
            return word;
        }

        Pattern separatorPattern = Pattern.compile(Separator.PATTERN);
        Matcher separatorMatcher = separatorPattern.matcher(codeBuffer);
        if (separatorMatcher.find(index) && separatorMatcher.start() == index) {
            String values = separatorMatcher.group(0);
            Word word = new Separator(Separator.separatorWords.get(values), values, lineNumber);
            index = separatorMatcher.end();
            return word;
        }

        Pattern formatStringPattern = Pattern.compile(FormatString.PATTERN);
        Matcher formatStringMatcher = formatStringPattern.matcher(codeBuffer);
        if (formatStringMatcher.find(index) && formatStringMatcher.start() == index) {
            String values = formatStringMatcher.group(0);
            Word word = new FormatString(CategoryCode.STRCON, values, lineNumber);
            index = formatStringMatcher.end();
            return word;
        }

        Pattern intConstPattern = Pattern.compile(IntConst.PATTERN);
        Matcher intConstMatcher = intConstPattern.matcher(codeBuffer);
        if (intConstMatcher.find(index) && intConstMatcher.start() == index) {
            String values = intConstMatcher.group(0);
            Word word = new FormatString(CategoryCode.INTCON, values, lineNumber);
            index = intConstMatcher.end();
            return word;
        }
        return null;
    }

    public boolean stripWord() {

        if (index >= codeLength) {
            return false;
        }
        if (Character.isWhitespace(codeBuffer.charAt(index))) {
            //  System.out.println("MATCH!");
            if (codeBuffer.charAt(index) == '\n') {
                lineNumber++;
            }
            index++;
            if (index >= codeLength) {
                return false;
            }

            return true;
        }

        Pattern commentPattern1 = Pattern.compile("//");
        Matcher commentMatcher1 = commentPattern1.matcher(codeBuffer);

        if (commentMatcher1.find(index) && commentMatcher1.start() == index) {
            while (codeBuffer.charAt(index) != '\n') {
                index++;
            }

            return true;
        }

        Pattern commentPattern2 = Pattern.compile("/[*]");
        Matcher commentMatcher2 = commentPattern2.matcher(codeBuffer);

        if (commentMatcher2.find(index) && commentMatcher2.start() == index) {
            index += 2;
            if (index >= codeLength) {
                return false;
            }
            Pattern commentPattern3 = Pattern.compile("[*]/");
            Matcher commentMatcher3 = commentPattern3.matcher(codeBuffer);
            if (commentMatcher3.find(index)) {
                int end = commentMatcher3.end();
                while (index < end) {
                    if (codeBuffer.charAt(index) == '\n') {
                        lineNumber++;
                    }
                    index++;
                }
            } else {
                // ERROR
            }
            return true;
        }

        return false;
    }

}
