import io.IOTool;
import lexicalanalysis.LexicalAnalyzer;

import java.io.IOException;

public class Compiler {

    public static void main(String[] args) {

        StringBuffer codeBuffer = new StringBuffer();

        try {
            IOTool.readFromFile(codeBuffer, "testfile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        LexicalAnalyzer myLexicalAnalyzer = new LexicalAnalyzer(codeBuffer);

        // System.out.println(codeBuffer);

    }

}


