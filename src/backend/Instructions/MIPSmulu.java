package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;

public class MIPSmulu extends MipsInstruction {

    private Reg rd;
    private Reg rs;
    private Reg rt;
    private Imm imm;
    private int type;

    public MIPSmulu(Reg rd, Reg rs, Reg rt) {
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
        this.type = 1;
    }

    public MIPSmulu(Reg rd, Reg rs, Imm imm) {
        this.rd = rd;
        this.rs = rs;
        this.imm = imm;
        this.type = 2;
    }

    @Override
    public String toString() {
        if (type == 1) {
            return "mulu " + rd.toString() + ", " + rs.toString() + ", " + rt.toString();
        } else {
            return "mulu " + rd.toString() + ", " + rs.toString() + ", " + imm.toString();
        }
    }
}
