package backend.Instructions;

import backend.operand.Reg;

public class MIPSIcmp extends MipsInstruction {

    public enum MIPSIcmpType {
        SEQ,
        SNE,
        SGT,
        SGE,
        SLT,
        SLE;

        @Override
        public String toString() {
            switch (this) {
                case SEQ: {
                    return "seq";
                }
                case SNE: {
                    return "sne";
                }
                case SGE: {
                    return "sge";
                }
                case SGT: {
                    return "sgt";
                }
                case SLE: {
                    return "sle";
                }
                case SLT: {
                    return "slt";
                }
                default: {
                    return null;
                }
            }
        }
    }


    private MIPSIcmpType mipsIcmpType;
    private Reg rd;
    private Reg rs;
    private Reg rt;

    public MIPSIcmp(MIPSIcmpType mipsIcmpType, Reg rd, Reg rs, Reg rt) {
        this.mipsIcmpType = mipsIcmpType;
        this.rd = rd;
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return mipsIcmpType.toString() + " " + rd.toString() + ", " + rs.toString() + ", "  + rt.toString();
    }
}
