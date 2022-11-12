package backend.Instructions;

import backend.operand.Reg;

public class MIPSdivu extends MipsInstruction {

    private Reg rs;
    private Reg rt;

    public MIPSdivu(Reg rs, Reg rt) {
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "divu " + rs.toString() + ", " + rt.toString();
    }
}
