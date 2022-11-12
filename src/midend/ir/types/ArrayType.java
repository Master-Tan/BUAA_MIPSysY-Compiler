package midend.ir.types;

import java.util.ArrayList;

public class ArrayType extends Type {

    // 数组降一维之后类型
    // 数组最多“一维”
    private Type elementType;

    private int elementNum;

    public ArrayType(Type elementType, int elementNum) {
        this.elementType = elementType;
        this.elementNum = elementNum;
    }

    public Type getElementType() {
        return elementType;
    }

    public int getElementNum() {
        return elementNum;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public String toString() {
        return "[" + this.elementNum + " x " + this.elementType.toString() + "]";
    }
}
