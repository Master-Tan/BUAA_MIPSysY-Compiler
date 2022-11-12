package midend.ir.values.instructions.binary;

import midend.ir.types.IntegerType;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Icmp extends BinaryOperatorInstruction {

    private IcmpType icmpType;  // 操作类型

    public Icmp(String name, BasicBlock parent, Value op1, Value op2, IcmpType icmpType) {
        super(name, new IntegerType(1), parent, op1, op2);
        this.icmpType = icmpType;
    }

    @Override
    public String toString() {
        return this.getName() + " = icmp " + this.icmpType.toString() + " " +
                this.getUsedValue(0).getType() + " " +
                this.getUsedValue(0).getName() + ", "  + this.getUsedValue(1).getName();
    }

}
