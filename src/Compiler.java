import exceptions.SysYException;
import grammaticalAnalysis.GrammaticalAnalyzer;
import grammaticalAnalysis.grammatical.Node;
import io.IOTool;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Compiler {

    public static void main(String[] args) {

        StringBuffer codeBuffer = new StringBuffer();

        try {
            IOTool.readFromFile(codeBuffer, "testfile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            IOTool.changeSystemoutToFile("output.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            IOTool.changeSystemErrToFile("error.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        LexicalAnalyzer myLexicalAnalyzer = new LexicalAnalyzer(codeBuffer);

        GrammaticalAnalyzer myGrammaticalAnalyzer =
                new GrammaticalAnalyzer(myLexicalAnalyzer.getWords());
        // System.out.println(codeBuffer);

        ArrayList<Pair<Integer, SysYException.ExceptionCode>> errors =
                new ArrayList<Pair<Integer, SysYException.ExceptionCode>>() {{
                    addAll(Node.errors);
                }};

        errors.sort(Comparator.comparing(o -> o.getKey()));
        for (Pair<Integer, SysYException.ExceptionCode> error : errors) {
            System.err.println(error.getKey() + " " + error.getValue());
        }
        
    }



}


