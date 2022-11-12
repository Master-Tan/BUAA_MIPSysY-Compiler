package backend.Parser;

import backend.Instructions.*;
import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;
import midend.ir.values.Module;

import java.util.ArrayList;
import java.util.HashMap;

public class MipsModule {

    private static final MipsModule mipsModule = new MipsModule();

    public static MipsModule getInstance() {
        return mipsModule;
    }

    // 所有全局变量
    private ArrayList<MipsGlobalVarible> mipsGlobalVaribles = new ArrayList<>();

    // 所有函数
    private ArrayList<MipsFunction> mipsFunctions = new ArrayList<>();


    public MipsModule() {
    }

    public void addMipsGlobalVarible(MipsGlobalVarible mipsGlobalVarible) {
        mipsGlobalVaribles.add(mipsGlobalVarible);
    }

    public void addMipsFunction(MipsFunction mipsFunction) {
        mipsFunctions.add(mipsFunction);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();


        // 数据段
        if (!mipsGlobalVaribles.isEmpty())
        {
            sb.append(".data\n");
            // 遍历每一个全局变量
            for (MipsGlobalVarible mipsGlobalVarible : mipsGlobalVaribles)
            {
                sb.append(mipsGlobalVarible.toString());
            }
        }
        sb.append("\n");

        // 代码段开始
        sb.append(".text\n");

        sb.append(new MIPSli(new Reg(30), new Imm(0x10040000)).toString() + "\n");

        sb.append(new MIPSj(new Label("main")).toString() + "\n");

        sb.append("\n");
        // 开始遍历每一个 function
        for (MipsFunction mipsFunction : mipsFunctions)
        {
            sb.append(mipsFunction.toString());
        }


        return sb.toString();
    }
}
