package backend.Instructions;

import backend.operand.Reg;

public class MIPSmflo extends MipsInstruction {

    private Reg rd;

    public MIPSmflo(Reg rd) {
        this.rd = rd;
    }

    @Override
    public String toString() {
        return "mflo " + rd.toString();
    }

}
