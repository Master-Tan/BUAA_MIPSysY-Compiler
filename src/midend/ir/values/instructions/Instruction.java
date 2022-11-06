package midend.ir.values.instructions;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.User;
import midend.ir.values.Value;

public class Instruction extends User {

    // 指令: 指令返回值存放的寄存器、指令返回值类型、指令所在的基本块，指令的操作数列表
    public Instruction(String name, Type type, BasicBlock parent, Value... ops) {
        super(name, type, parent, ops);
    }
}
