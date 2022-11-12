package midend.ir.values.instructions.memory;

import midend.ir.types.ArrayType;
import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;

public class Getelementptr extends MemoryInstruction {

    private Type baseType;  // 数组类型

    // 数组传参，操作数：基址, 指针寻址下标
    public Getelementptr(String name, ArrayType baseType, BasicBlock parent, Value base, Value pointIndex) {
        super("%" + name, new PointerType(baseType), parent, base, pointIndex);
        this.baseType = baseType;
    }

    public Getelementptr(String name, PointerType baseType, BasicBlock parent, Value base, Value pointIndex) {
        super("%" + name, baseType, parent, base, pointIndex);
        this.baseType = baseType.getPointToType();
    }

    // 令 getelemtnptr 每次只寻址一次，操作数：基址、指针寻址下标、数组寻址下标
    public Getelementptr(String name, ArrayType baseType, BasicBlock parent, Value base, Value pointIndex, Value arrayIndex) {
        super("%" + name, new PointerType(baseType.getElementType()), parent, base, pointIndex, arrayIndex);
        this.baseType = baseType;
    }

    public Getelementptr(String name, PointerType baseType, BasicBlock parent, Value base, Value pointIndex, Value arrayIndex) {
        super("%" + name, new PointerType(((ArrayType) baseType.getPointToType()).getElementType()), parent, base, pointIndex, arrayIndex);
        this.baseType = baseType.getPointToType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(" = getelementptr inbounds ");
        sb.append(baseType);
        sb.append(", ");
        for (int i = 0; i < this.getValueNum(); i++) {
            sb.append(this.getUsedValue(i).getType());
            sb.append(" ");
            sb.append(this.getUsedValue(i).getName());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
