package midend.ir.values.instructions.memory;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Phi extends MemoryInstruction {

    public Phi(String name, Type type, BasicBlock parent, Value... ops) {
        super(name, type, parent, ops);
    }
    // TODO 分支或循环时用

}
