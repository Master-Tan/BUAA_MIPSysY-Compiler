package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;

public class MIPSla extends MipsInstruction {

    private Reg reg;
    private Label label;

    public MIPSla(Reg reg, Label label) {
        this.reg = reg;
        this.label = label;
    }

    @Override
    public String toString() {
        return "la " + reg.toString() + ", " + label.toString();
    }

}
