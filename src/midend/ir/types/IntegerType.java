package midend.ir.types;

public class IntegerType extends Type {

    private int bits;

    public IntegerType(int bits) {
        this.bits = bits;
    }

    @Override
    public String toString() {
        return "i" + bits;
    }

    @Override
    public int getSize() {
        return 4;
    }
}
