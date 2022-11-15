package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;

public class MIPSbeq extends MipsInstruction {

    private Reg rd;
    private Imm imm;
    private Label label;

    public MIPSbeq(Reg rd, Imm imm, Label label) {
        this.rd = rd;
        this.imm = imm;
        this.label = label;
    }

    @Override
    public String toString() {
        return "beq " + rd.toString() + ", " + imm.toString() + ", " + label.toString();
    }
}
