import grammaticalAnalysis.GrammaticalAnalyzer;
import io.IOTool;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.lexical.Word;

import java.io.IOException;
import java.util.ArrayList;

public class Compiler {

    public static void main(String[] args) {

        StringBuffer codeBuffer = new StringBuffer();

        try {
            IOTool.readFromFile(codeBuffer, "testfile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            IOTool.changeSystemoutToFile("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        LexicalAnalyzer myLexicalAnalyzer = new LexicalAnalyzer(codeBuffer);

        GrammaticalAnalyzer myGrammaticalAnalyzer =
                new GrammaticalAnalyzer(myLexicalAnalyzer.getWords());
        // System.out.println(codeBuffer);

    }

}


