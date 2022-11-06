package midend.ir.types;

public class LabelType extends Type {

    @Override
    public String toString() {
        return "label";
    }

    @Override
    public int getSize() {
        return 0;
    }
}
