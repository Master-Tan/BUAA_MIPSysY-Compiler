package midend.ir.values.instructions.terminator;

import midend.ir.types.VoidType;
import midend.ir.values.Argument;
import midend.ir.values.BasicBlock;
import midend.ir.values.Value;
import midend.ir.values.constant.Function;

import java.util.ArrayList;

public class Call extends TerminatorInstruction {

    private boolean isVoid;

    // 无返回值
    // 操作数为函数，所有函数实参
    public Call(BasicBlock parent, Function function, ArrayList<Value> args) {
        super("", new VoidType(), parent, new ArrayList<Value>() {{
            add(function);
            addAll(args);
        }}.toArray(new Value[0]));
        isVoid = true;
    }

    // 有返回值
    // 操作数为函数，所有函数实参
    public Call(String name, BasicBlock parent, Function function, ArrayList<Value> args) {
        super("%" + name, function.getReturnType(), parent, new ArrayList<Value>() {{
            add(function);
            addAll(args);
        }}.toArray(new Value[args.size() + 1]));
        isVoid = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isVoid) {
            sb.append("call ");
        } else {
            sb.append(this.getName());
            sb.append(" = call ");
        }
        sb.append(this.getType());
        sb.append(" ");
        sb.append(this.getUsedValue(0).getName());
        sb.append("(");
        int numArgs = ((Function) this.getUsedValue(0)).getArguments().size();
        for (int i = 1; i <= numArgs; i++) {
            sb.append(this.getUsedValue(i).getType());
            sb.append(" ");
            sb.append(this.getUsedValue(i).getName());
            sb.append(", ");
        }
        if (!((Function) this.getUsedValue(0)).getArguments().isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
        return sb.toString();
    }
}
