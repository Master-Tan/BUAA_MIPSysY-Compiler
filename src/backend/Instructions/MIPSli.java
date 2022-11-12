package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Reg;

public class MIPSli extends MipsInstruction {

    private Reg reg;
    private Imm imm;

    public MIPSli(Reg reg, Imm imm) {
        this.reg = reg;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return "li " + reg.toString() + ", " + imm.toDecString();
    }
}

