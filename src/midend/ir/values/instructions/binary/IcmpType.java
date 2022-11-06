package midend.ir.values.instructions.binary;

public enum IcmpType {
    EQ,
    NE,
    SGT,
    SGE,
    SLT,
    SLE;

    @Override
    public String toString() {
        switch (this) {
            case EQ: {
                return "eq";
            }
            case NE: {
                return "ne";
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
