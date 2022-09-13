package lexicalanalysis;

import io.IOTool;
import lexicalanalysis.lexical.Word;

import java.io.IOException;
import java.util.ArrayList;

public class LexicalAnalyzer {

    private StringBuffer codeBuffer;  // 原本的代码

    private Token token;  // 代码取词器

    private ArrayList<Word> words;  // 识别出的单词

    public LexicalAnalyzer(StringBuffer codeBuffer) {

        this.codeBuffer = codeBuffer;
        token = new Token(codeBuffer);
        words = new ArrayList<>();
        this.analyse();

        outputAnalysisResult();

    }

    private void analyse() {
        Word word;
        word = token.nextWord();
        while (word != null) {
            System.out.println(word);
            words.add(word);
            word = token.nextWord();
        }
    }

    public void outputAnalysisResult() {
        try {
            IOTool.changeSystemoutToFile("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Word word : words) {
            System.out.println(word.getCategoryCode() + " " + word.getWordValue());
        }

    }
}
