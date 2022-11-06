package midend.ir.values.instructions.memory;

import midend.ir.types.Type;
import midend.ir.types.VoidType;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Store extends MemoryInstruction {

    // 操作数为存储数值，存储地址
    public Store(BasicBlock parent, Value storeVal, Value location) {
        super("", new VoidType(), parent, storeVal, location);
    }

    @Override
    public String toString() {
        return "store " + this.getUsedValue(0).getType() + " " + this.getUsedValue(0).getName() +
                ", " + this.getUsedValue(1).getType() + " " + this.getUsedValue(1).getName();
    }
}
