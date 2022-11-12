package backend.Instructions;

import backend.operand.Reg;

public class MIPSjr extends MipsInstruction {

    Reg reg;

    public MIPSjr(Reg reg) {
        this.reg = reg;
    }

    @Override
    public String toString() {
        return "jr " + reg.toString();
    }
}
