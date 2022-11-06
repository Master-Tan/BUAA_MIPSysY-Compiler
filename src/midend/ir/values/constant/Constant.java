package midend.ir.values.constant;

import midend.ir.types.IntegerType;
import midend.ir.types.Type;
import midend.ir.values.User;
import midend.ir.values.Value;

public class Constant extends User {

    // 常量

    public Constant(String name, Type type, Value parent, Value... values) {
        super(name, type, parent, values);
    }

    public static Value getZeroConstant(Type type) {
        if (type instanceof IntegerType) {
            return new ConstantInt(32, 0);
        } else {
            // 数组类型
            // TODO
            return null;
        }
    }
}
