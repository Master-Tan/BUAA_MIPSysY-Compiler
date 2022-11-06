package midend.ir.values.instructions;

public enum OP {

    // binary
    Add,
    And,
    Icmp,
    Mul,
    Or,
    Sdiv,
    Srem,
    Sub,

    // memory
    Alloca,
    Load,
    Phi,
    Store,
    ZextTo,

    // terminator
    Br,
    Call,
    Ret

}
