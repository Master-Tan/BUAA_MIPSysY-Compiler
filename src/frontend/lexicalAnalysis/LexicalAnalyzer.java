package frontend.lexicalAnalysis;

import frontend.lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class LexicalAnalyzer {

    public static final boolean printLexicalAnalyzerData = false;  // 是否输出词法分析结果

    private StringBuffer codeBuffer;  // 原本的代码

    private Token token;  // 代码取词器

    private ArrayList<Word> words;  // 识别出的单词

    public LexicalAnalyzer(StringBuffer codeBuffer) {

        this.codeBuffer = codeBuffer;
        token = new Token(this.codeBuffer);

         words = new ArrayList<>();
         this.analyse();
        // outputAnalysisResult();

    }

    private void analyse() {
        Word word;
        word = token.nextWord();
        while (word != null) {
//            System.out.println(word);
            words.add(word);
            word = token.nextWord();
        }
    }

    public void outputAnalysisResult() {

        for (Word word : words) {
            System.out.println(word.getCategoryCode() + " " + word.getWordValue());
        }

    }

    public Word nextWord() {
        Word word = token.nextWord();
        return word;
    }

    public ArrayList<Word> getWords() {
        return words;
    }
}
