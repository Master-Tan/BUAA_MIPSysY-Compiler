package midend.ir.values.constant;

import midend.ir.types.Type;
import midend.ir.values.Value;

public class ConstantArray extends Constant {

    // 数组常量

    public ConstantArray(String name, Type type, Value parent, Value... values) {
        super(name, type, parent, values);
        // TODO
    }
}
