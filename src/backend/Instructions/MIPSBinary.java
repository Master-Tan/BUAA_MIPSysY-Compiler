package backend.Instructions;

import backend.operand.Reg;

public class MIPSBinary extends MipsInstruction {

    private MIPSBinaryType mipsBinaryType;
    private Reg rd;
    private Reg rs;
    private Reg rt;

    public MIPSBinary(MIPSBinaryType mipsBinaryType, Reg rd, Reg rs, Reg rt) {
        this.mipsBinaryType = mipsBinaryType;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return mipsBinaryType.toString() + " " + rd.toString() + ", " + rs.toString() + ", " + rt.toString();
    }
}
