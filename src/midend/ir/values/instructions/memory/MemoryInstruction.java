package midend.ir.values.instructions.memory;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;
import midend.ir.values.instructions.Instruction;

public class MemoryInstruction extends Instruction {

    public MemoryInstruction(String name, Type type, BasicBlock parent, Value... ops) {
        super(name, type, parent, ops);
    }

}
