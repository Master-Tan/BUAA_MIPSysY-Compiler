package midend.ir.values.instructions.memory;

import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Getelementptr extends MemoryInstruction {

    private Value baseType;
    private Value pointType;

    // 令 getelemtnptr 每次只寻址一次，只有2个操作数为：基址、下标
    public Getelementptr(String name, Type type, BasicBlock parent, Value... ops) {
        super(name, type, parent, ops);
    }
    // TODO


}
