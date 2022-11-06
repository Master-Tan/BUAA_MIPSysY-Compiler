package midend.ir.types;

public class VoidType extends Type {

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public int getSize() {
        return 0;
    }
}
