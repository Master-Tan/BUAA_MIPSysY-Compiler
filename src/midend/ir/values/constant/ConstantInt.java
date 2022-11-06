package midend.ir.values.constant;

import midend.ir.types.IntegerType;
import midend.ir.types.Type;
import midend.ir.values.Value;

public class ConstantInt extends Constant {

    // 整数常量

    private int val;  // 保存整数的值

    public ConstantInt(int bits, int val) {
        super(Integer.toString(val), new IntegerType(bits), null);
    }

    public int getVal() {
        return val;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
