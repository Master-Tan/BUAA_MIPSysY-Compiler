package midend.ir.values.instructions.terminator;

import midend.ir.types.Type;
import midend.ir.types.VoidType;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Ret extends TerminatorInstruction {

    private boolean isVoid;

    // 返回void
    // 无操作数
    public Ret(BasicBlock parent) {
        super("", new VoidType(), parent);
        isVoid = true;
    }

    // 返回不为void
    // 操作数为返回值
    public Ret(BasicBlock parent, Value returnVal) {
        super("", returnVal.getType(), parent, returnVal);
        isVoid = false;
    }

    @Override
    public String toString() {
        if (isVoid) {
            return "ret void";
        } else {
            return "ret " + this.getUsedValue(0).getType() + " " + this.getUsedValue(0).getName();
        }
    }
}
