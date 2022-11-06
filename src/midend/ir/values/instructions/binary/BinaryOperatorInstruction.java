package midend.ir.values.instructions.binary;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;
import midend.ir.values.instructions.Instruction;

public class BinaryOperatorInstruction extends Instruction {

    // 二元运算符指令
    // 操作数为两运算数，nameNum：寄存器数
    public BinaryOperatorInstruction(String name, Type type, BasicBlock parent, Value op1, Value op2) {
        super("%" + name, type, parent, op1, op2);
    }

}
