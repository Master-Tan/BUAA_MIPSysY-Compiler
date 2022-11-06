package midend.ir.values.instructions.terminator;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;
import midend.ir.values.instructions.Instruction;

public class TerminatorInstruction extends Instruction {

    public TerminatorInstruction(String name, Type type, BasicBlock parent, Value... ops) {
        super(name, type, parent, ops);
    }
}
