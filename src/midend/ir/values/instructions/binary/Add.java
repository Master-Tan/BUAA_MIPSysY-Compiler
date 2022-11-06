package midend.ir.values.instructions.binary;

import midend.ir.types.IntegerType;
import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Add extends BinaryOperatorInstruction {

    public Add(String name, BasicBlock parent, Value op1, Value op2) {
        super(name, new IntegerType(32), parent, op1, op2);
    }

    @Override
    public String toString() {
        return this.getName() + " = add " + this.getType() + " " +
                this.getUsedValue(0).getName() + ", "  + this.getUsedValue(1).getName();
    }
}
