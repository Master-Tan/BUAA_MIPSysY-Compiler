package backend.Parser;

import backend.Instructions.MIPSjr;
import backend.Instructions.MIPSli;
import backend.Instructions.MIPSsyscall;
import backend.operand.Imm;
import backend.operand.Reg;
import midend.ir.types.PointerType;
import midend.ir.values.Argument;
import midend.ir.values.constant.Function;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsFunction {

    private String name;
    private ArrayList<MipsBasicBlock> mipsBasicBlocks = new ArrayList<>();
    private Function function;

    private int functionFP;

    // <name, fpOffset>
    private HashMap<String, Integer> nameToFp = new HashMap<>();
    // <pointer, fpOffset>
    private HashMap<String, Integer> pointToFp = new HashMap<>();
    // the fp store fp?
    private ArrayList<String> isArrayIndex = new ArrayList<>();

    public MipsFunction(String name, Function function) {
        this.name = name;
        this.function = function;
        this.functionFP = 0;

        for (Argument argument : function.getArguments()) {
            nameToFp.put(argument.getName().substring(1), functionFP);
            if (argument.getType() instanceof PointerType) {
                isArrayIndex.add(argument.getName().substring(1));
            }
            functionFP += 4;
        }
    }

    public String getName() {
        return name;
    }

    public void addMipsBasicBlock(MipsBasicBlock mipsBasicBlock) {
        mipsBasicBlocks.add(mipsBasicBlock);
    }

    public HashMap<String, Integer> getNameToFp() {
        return nameToFp;
    }

    public void addNameToFp(String name, int fpOffset) {
        nameToFp.put(name, fpOffset);
    }

    public HashMap<String, Integer> getPointToFp() {
        return pointToFp;
    }

    public void addPointToFp(String name, int fpOffset) {
        pointToFp.put(name, fpOffset);
    }

    public int getFunctionFP() {
        return functionFP;
    }

    public void addFunctionFP(int offset) {
        functionFP += offset;
    }

    public void addIsArrayIndex(String name) {
        this.isArrayIndex.add(name);
    }

    public ArrayList<String> getIsArrayIndex() {
        return isArrayIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (!function.isBuiltIn()) {
            // 输出函数名
            sb.append(this.name + ":\n");

            // 输出block
            for (MipsBasicBlock mipsBasicBlock : mipsBasicBlocks) {
                sb.append(mipsBasicBlock.toString());
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}
