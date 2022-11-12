package midend.ir.values.constant;

import midend.ir.types.ArrayType;

public class ZeroInitializer extends Constant {

    // 数组的零类型

    private int elementNum;

    public ZeroInitializer(ArrayType arrayType) {
        super("zeroinitializer", arrayType, null);
        elementNum = arrayType.getElementNum();
    }

    @Override
    public String toString() {
        return "zeroinitializer";
    }

    public int getLength() {
        return elementNum;
    }

}
