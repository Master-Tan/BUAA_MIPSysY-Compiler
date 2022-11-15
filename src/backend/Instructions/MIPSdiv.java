package backend.Instructions;

import backend.operand.Reg;

public class MIPSdiv extends MipsInstruction {

    private Reg rs;
    private Reg rt;

    public MIPSdiv(Reg rs, Reg rt) {
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "div " + rs.toString() + ", " + rt.toString();
    }
}
