import backend.Backend;
import exceptions.SysYException;
import frontend.Visitor;
import frontend.grammaticalAnalysis.GrammaticalAnalyzer;
import frontend.grammaticalAnalysis.grammatical.Node;
import io.IOTool;
import frontend.lexicalAnalysis.LexicalAnalyzer;
import midend.ir.values.Module;
import myclasses.Pair;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;

public class Compiler {

    public static void main(String[] args) {

        PrintStream systemOut = System.out;
        PrintStream systemErr = System.err;

        StringBuffer codeBuffer = new StringBuffer();

        try {
            IOTool.readFromFile(codeBuffer, "testfile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 词法分析、语法分析
        try {
            IOTool.changeSystemoutToFile("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LexicalAnalyzer myLexicalAnalyzer = new LexicalAnalyzer(codeBuffer);

        GrammaticalAnalyzer myGrammaticalAnalyzer =
                new GrammaticalAnalyzer(myLexicalAnalyzer.getWords());
        // System.out.println(codeBuffer);

        // 错误处理
        try {
            IOTool.changeSystemErrToFile("error.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Pair<Integer, SysYException.ExceptionCode>> errors =
                new ArrayList<Pair<Integer, SysYException.ExceptionCode>>() {{
                    addAll(Node.errors);
                }};
        errors.sort(Comparator.comparing(o -> o.getKey()));
        for (Pair<Integer, SysYException.ExceptionCode> error : errors) {
            System.err.println(error.getKey() + " " + error.getValue());
        }

        System.setErr(systemErr);

//        if (errors.size() > 0) {
//            System.err.println("has error!");
//            return;
//        }

        // LLVM IR 生成
//        try {
//            IOTool.changeSystemoutToFile("llvm_ir.txt");
////            System.setOut(systemOut);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Module myModule = Module.getInstance();
//
//        Visitor visitor = new Visitor();
//        visitor.visitCompUnit(myGrammaticalAnalyzer.getCompUnitNode());
//
//        System.out.println(myModule.toString());
//
//        // MIPS 生成
//        try {
//            IOTool.changeSystemoutToFile("mips.txt");
////            System.setOut(systemOut);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Backend backend = new Backend();
//        System.out.println(backend.getMIPS());

    }

}