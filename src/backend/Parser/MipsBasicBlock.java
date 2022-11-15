package backend.Parser;

import backend.Instructions.MipsInstruction;
import midend.ir.values.BasicBlock;

import java.util.ArrayList;

public class MipsBasicBlock {

    private String name;
    private MipsFunction mipsFunction;
    private ArrayList<MipsInstruction> mipsInstructions = new ArrayList<>();
    private BasicBlock basicBlock;

    public MipsBasicBlock(String name, BasicBlock basicBlock, MipsFunction mipsFunction) {
        this.name = name;
        this.basicBlock = basicBlock;
        this.mipsFunction = mipsFunction;
    }

    public void addMipsInstruction(MipsInstruction mipsInstruction) {
        mipsInstructions.add(mipsInstruction);
//        System.out.println(mipsInstruction);
    }

    public MipsFunction getMipsFunction() {
        return mipsFunction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.name);
        sb.append(":\n");

        // 2. 按序输出指令
        for (MipsInstruction mipsInstruction : mipsInstructions) {
            sb.append("\t" + mipsInstruction.toString() + "\n");
        }


        return sb.toString();
    }
}
