package backend.Instructions;

import backend.operand.Imm;
import backend.operand.Reg;

public class MIPSBinaryI extends MipsInstruction {

    private MIPSBinaryIType mipsBinaryIType;
    private Reg rd;
    private Reg rs;
    private Imm imm;

    public MIPSBinaryI(MIPSBinaryIType mipsBinaryIType, Reg rd, Reg rs, Imm imm) {
        this.mipsBinaryIType = mipsBinaryIType;
        this.rd = rd;
        this.rs = rs;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return mipsBinaryIType.toString() + " " + rd.toString() + ", " + rs.toString() + ", " + imm.toDecString();
    }

}
