package backend.operand;

public class Imm extends MipsOperand {

    int val;

    public Imm(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }

    public String toHexString() {
        return "0x" + Integer.toHexString(val);
    }

    public String toDecString() {
        return Integer.toString(val);
    }
}
