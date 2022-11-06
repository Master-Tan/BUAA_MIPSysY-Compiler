package midend.ir.values;

import midend.ir.types.LabelType;
import midend.ir.types.Type;
import midend.ir.values.constant.Function;
import midend.ir.values.instructions.Instruction;

import java.util.ArrayList;

public class BasicBlock extends Value {

    // 基本块

    private ArrayList<Instruction> instructions = new ArrayList<>();

    public BasicBlock(String name, Value parent) {
        super("%" + name, new LabelType(), parent);
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public Function getParent() {
        return ((Function) super.getParent());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName().substring(1));
        sb.append(":");
        sb.append("\n");
        for (Instruction instruction : instructions) {
            sb.append("\t");
            sb.append(instruction.toString());
            sb.append("\n");
        }
        if (!instructions.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
}
