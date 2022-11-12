package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;

public class MIPSsw extends MipsInstruction {

    private Reg rt;
    private Imm offset;
    private Reg base;
    private Label label;
    private int type;

    public MIPSsw(Reg rt, Imm offset, Reg base) {
        this.rt = rt;
        this.offset = offset;
        this.base = base;
        this.type = 1;
    }

    public MIPSsw(Reg rt, Label label) {
        this.rt = rt;
        this.label = label;
        this.type = 2;
    }

    @Override
    public String toString() {
        if (type == 1) {
            return "sw " + rt.toString() + ", " + offset.toString() + "(" + base.toString() + ")";
        } else {
            return "sw " + rt.toString() + ", " + label.toString();
        }
    }

}
