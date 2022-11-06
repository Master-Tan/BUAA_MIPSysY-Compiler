package midend.ir.values.instructions.memory;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Load extends MemoryInstruction {

    private Type type;

    // 操作数：地址
    public Load(String name, Type type, BasicBlock parent, Value location) {
        super("%" + name, type, parent, location);
        this.type = type;
    }

    @Override
    public String toString() {
        return this.getName() + " = load " + this.getType() + ", " +
                this.getUsedValue(0).getType() + " " + this.getUsedValue(0).getName();
    }
}
