import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Compiler {

    public static void main(String[] args) {

        ArrayList<String> codes = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("testfile.txt"))) {
            String str;
            while ((str = br.readLine()) != null) {
                codes.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(codes);

        FileWriter fw;
        try {
            fw = new FileWriter("output.txt");
            for (String code : codes) {
                fw.write(code + '\n');
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
