package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;

public class MIPSlw extends MipsInstruction {

    private Reg rt;
    private Imm offset;
    private Reg base;
    private Label label;
    private int type;

    public MIPSlw(Reg rt, Imm offset, Reg base) {
        this.rt = rt;
        this.offset = offset;
        this.base = base;
        this.type = 1;
    }

    public MIPSlw(Reg rt, Label label) {
        this.rt = rt;
        this.label = label;
        this.type = 2;
    }

    @Override
    public String toString() {
        if (type == 1) {
            return "lw " + rt.toString() + ", " + offset.toString() + "(" + base.toString() + ")";
        } else {
            return "lw " + rt.toString() + ", " + label.toString();
        }
    }
}
