package midend.ir.values.constant;

import midend.ir.types.ArrayType;
import midend.ir.types.IntegerType;
import midend.ir.types.Type;
import midend.ir.values.Value;

import java.util.ArrayList;

public class ConstantString extends Constant {

    private String string;

    // 字符串常量

    public ConstantString(String str) {
        super("c\"" + checkChangeLine(str) + "\\00\"", new ArrayType(new IntegerType(8), str.length() + 1), null);
        this.string = str;
    }

    private static String checkChangeLine(String str) {
        StringBuilder sb = new StringBuilder();
        char[] stringArray = str.toCharArray();
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i] == '\n') {
                sb.append("\\0A");
                continue;
            }
            sb.append(stringArray[i]);
        }
        return sb.toString();
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        char[] stringArray = this.string.toCharArray();
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i] == '\n') {
                sb.append("\\n");
                continue;
            }
            sb.append(stringArray[i]);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
