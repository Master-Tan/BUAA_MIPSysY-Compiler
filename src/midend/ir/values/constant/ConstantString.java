package midend.ir.values.constant;

import midend.ir.types.ArrayType;
import midend.ir.types.Type;
import midend.ir.values.Value;

public class ConstantString extends Constant {

    // 字符串常量

    public ConstantString(String str) {
        super("c\"" + str + "\\00\"", new ArrayType(), null);
        // TODO
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
