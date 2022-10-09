package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class IOTool {

    public static void readFromFile(StringBuffer buffer, String filePath) throws IOException {
        File source = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(source));
        String str;
        while ((str = br.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        buffer.deleteCharAt(buffer.length() - 1);
    }

    public static void changeSystemoutToFile(String outputPath) throws IOException {
        PrintStream out = new PrintStream(outputPath);
        System.setOut(out);
    }

    public static void changeSystemErrToFile(String outputPath) throws IOException {
        PrintStream out = new PrintStream(outputPath);
        System.setErr(out);
    }

}
