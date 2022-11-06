package midend.ir.types;

public class PointerType extends Type {

    // 记录指针信息

    private Type pointToType;

    public PointerType(Type pointToType) {
        this.pointToType = pointToType;
    }

    public Type getPointToType() {
        return pointToType;
    }

    @Override
    public String toString() {
        return pointToType.toString() + "*";
    }

    @Override
    public int getSize() {
        return 4;
    }
}
