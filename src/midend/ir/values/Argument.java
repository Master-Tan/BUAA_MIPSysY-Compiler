package midend.ir.values;

import midend.ir.types.Type;

public class Argument extends Value {

    // 函数形参
    public Argument(String name, Type type, Value parent) {
        super("%" + name, type, parent);
    }

}
