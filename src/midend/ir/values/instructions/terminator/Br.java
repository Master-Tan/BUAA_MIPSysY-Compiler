package midend.ir.values.instructions.terminator;

import midend.ir.types.Type;
import midend.ir.types.VoidType;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Br extends TerminatorInstruction {

    private boolean isCondition;

    // 无条件跳转
    // 操作数为目标基本块
    public Br(BasicBlock parent, BasicBlock toBB) {
        super("", new VoidType(), parent, toBB);
        isCondition = false;
    }

    // 条件跳转
    // 操作数为条件，两个分支基本块
    public Br(BasicBlock parent, Value condition, BasicBlock trueBB, BasicBlock falseBB) {
        super("", new VoidType(), parent, condition, trueBB, falseBB);
        isCondition = true;
    }

    public boolean isCondition() {
        return isCondition;
    }

    @Override
    public String toString() {
        if (this.isCondition) {
            return "br " + this.getUsedValue(0).getType() + " " + this.getUsedValue(0).getName() +
                    ", " + this.getUsedValue(1).getType() + " " + this.getUsedValue(1).getName() +
                    ", " + this.getUsedValue(2).getType() + " " + this.getUsedValue(2).getName();
        } else {
            return "br " + this.getUsedValue(0).getType() + " " + this.getUsedValue(0).getName();
        }
    }
}
