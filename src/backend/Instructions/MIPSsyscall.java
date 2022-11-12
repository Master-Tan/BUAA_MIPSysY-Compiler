package backend.Instructions;

import midend.ir.values.BasicBlock;

public class MIPSsyscall extends MipsInstruction {

    @Override
    public String toString() {
        return "syscall";
    }

}
