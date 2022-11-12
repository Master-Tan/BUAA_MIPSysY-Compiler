package midend.ir.values.constant;

import backend.operand.Imm;
import midend.ir.types.FunctionType;
import midend.ir.types.IntegerType;
import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.types.VoidType;
import midend.ir.values.Argument;
import midend.ir.values.BasicBlock;
import midend.ir.values.Module;
import midend.ir.values.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Function extends Constant {

    // 函数，由若干个基本块组成

    private ArrayList<Argument> arguments = new ArrayList<>();

    private ArrayList<BasicBlock> basicBlocks = new ArrayList<>();
    private boolean isBuiltIn;


    public Function(String name, FunctionType functionType, boolean isBuiltIn) {
        super("@" + name, functionType, Module.getInstance());
        this.isBuiltIn = isBuiltIn;
        int argCnt = 0;
        for (Type arg : ((FunctionType) this.getType()).getArgs()) {
            Argument argument = new Argument("_" + argCnt, arg, this);
            this.arguments.add(argument);
            argCnt++;
        }
    }

    private static final HashMap<String, Function> builtInFunctionList = new HashMap<>();
    static {

        Function getint = new Function("getint", new FunctionType(new ArrayList<>(), new IntegerType(32)), true);
        builtInFunctionList.put("getint", getint);

        Function putint = new Function("putint", new FunctionType(new ArrayList<>(Arrays.asList(new IntegerType(32))), new VoidType()), true);
        builtInFunctionList.put("putint", putint);

        Function putch = new Function("putch", new FunctionType(new ArrayList<>(Arrays.asList(new IntegerType(32))), new VoidType()), true);
        builtInFunctionList.put("putch", putch);

        Function putstr = new Function("putstr", new FunctionType(new ArrayList<>(Arrays.asList(new PointerType(new IntegerType(8)))), new VoidType()), true);
        builtInFunctionList.put("putstr", putstr);

        for (Function function : builtInFunctionList.values()) {
            Module.getInstance().addFunction(function);
        }
    }

    public void addBasicBlock(BasicBlock basicBlock) {
        this.basicBlocks.add(basicBlock);
    }

    public Type getReturnType() {
        return ((FunctionType) this.getType()).getReturnType();
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public ArrayList<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isBuiltIn ? "declare" : "\ndefine");
        // sb.append(" dso_local ");
        sb.append(" ");
        sb.append(((FunctionType) this.getType()).getReturnType().toString());
        sb.append(" ");
        sb.append(this.getName());
        sb.append("(");
        for (Argument argument : this.arguments) {
            sb.append(argument.getType());
            sb.append(" ");
            if (!isBuiltIn) {
                sb.append(argument.getName());
                sb.append(", ");
            }
        }
        if (!arguments.isEmpty()) {
            if (!isBuiltIn) {
                sb.delete(sb.length() - 2, sb.length());
            } else {
                sb.delete(sb.length() - 1, sb.length());
            }
        }
        sb.append(")");
        sb.append(" ");
        if (!isBuiltIn) {
            sb.append("{");
            sb.append("\n");
            for (BasicBlock basicBlock : basicBlocks) {
                sb.append(basicBlock.toString());
                sb.append("\n");
            }
            sb.append("}");
        }
        return sb.toString();
    }
}
