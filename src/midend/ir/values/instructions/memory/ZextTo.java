package midend.ir.values.instructions.memory;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class ZextTo extends MemoryInstruction {

    // 操作数：要转换的值
    public ZextTo(String name, BasicBlock parent, Value zextVal, Type toType) {
        super("%" + name, toType, parent, zextVal);
    }

    @Override
    public String toString() {
        return this.getName() + " = zext " +
                this.getUsedValue(0).getType() + " " + this.getUsedValue(0).getName() +
                " to " + this.getType();
    }
}
