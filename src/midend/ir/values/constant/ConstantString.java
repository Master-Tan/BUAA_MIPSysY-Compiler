package midend.ir.values.constant;

import midend.ir.types.ArrayType;
import midend.ir.types.IntegerType;
import midend.ir.types.Type;
import midend.ir.values.Value;

import java.util.ArrayList;

public class ConstantString extends Constant {

    // 字符串常量

    public ConstantString(String str) {
        super("c\"" + str + "\\00\"", new ArrayType(new IntegerType(8), str.length() + 1), null);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
