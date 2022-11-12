package backend.Instructions;

import backend.operand.Reg;

public class MIPSmfhi extends MipsInstruction {

    private Reg rd;

    public MIPSmfhi(Reg rd) {
        this.rd = rd;
    }

    @Override
    public String toString() {
        return "mfhi " + rd.toString();
    }
}
