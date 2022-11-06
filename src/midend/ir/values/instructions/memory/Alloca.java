package midend.ir.values.instructions.memory;

import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Alloca extends MemoryInstruction {

    // 无操作数
    public Alloca(String name, Type allocatedType, BasicBlock parent) {
        super("%" + name, new PointerType(allocatedType), parent);
    }

    @Override
    public String toString() {
        return this.getName() + " = alloca " + ((PointerType) this.getType()).getPointToType();
    }
}
