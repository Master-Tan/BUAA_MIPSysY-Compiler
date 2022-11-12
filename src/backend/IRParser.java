package backend;

import backend.Instructions.*;
import backend.Parser.*;
import backend.operand.Imm;
import backend.operand.Label;
import backend.operand.Reg;
import midend.ir.types.IntegerType;
import midend.ir.types.PointerType;
import midend.ir.values.BasicBlock;
import midend.ir.values.Module;
import midend.ir.values.constant.ConstantInt;
import midend.ir.values.constant.Function;
import midend.ir.values.constant.GlobalVariable;
import midend.ir.values.instructions.Instruction;
import midend.ir.values.instructions.binary.*;
import midend.ir.values.instructions.memory.*;
import midend.ir.values.instructions.terminator.*;

public class IRParser {

    Module module;
    MipsModule mipsModule;

    public IRParser() {
        module = Module.getInstance();
        mipsModule = MipsModule.getInstance();
    }

    public void parserModule() {

        // 处理全局变量
        for (GlobalVariable globalVariable : module.getGlobalVariables()) {
            MipsGlobalVarible mipsGlobalVarible = new MipsGlobalVarible(globalVariable.getName().substring(1), globalVariable);
            mipsModule.addMipsGlobalVarible(mipsGlobalVarible);
            parserGlobalVarible(globalVariable, mipsGlobalVarible);
        }

        // 处理函数
        for (Function function : module.getFunctions()) {
            MipsFunction mipsFunction = new MipsFunction(function.getName().substring(1), function);
            mipsModule.addMipsFunction(mipsFunction);
            parserFunction(function, mipsFunction);
        }

    }

    private void parserGlobalVarible(GlobalVariable globalVariable, MipsGlobalVarible mipsGlobalVarible) {

    }

    private void parserFunction(Function function, MipsFunction mipsFunction) {

        // 处理基本块
        for (BasicBlock basicBlock : function.getBasicBlocks()) {
            MipsBasicBlock mipsBasicBlock = new MipsBasicBlock(basicBlock.getName().substring(1), basicBlock, mipsFunction);
            mipsFunction.addMipsBasicBlock(mipsBasicBlock);
            parserBasicBlock(basicBlock, mipsBasicBlock);
        }
    }

    private void parserBasicBlock(BasicBlock basicBlock, MipsBasicBlock mipsBasicBlock) {

        MipsFunction mipsFunction = mipsBasicBlock.getMipsFunction();

        // 处理指令
        for (Instruction instruction : basicBlock.getInstructions()) {
            if (instruction instanceof Add) {
                buildBinaryInstruction(instruction, mipsFunction, mipsBasicBlock, MIPSBinaryType.addu);
            } else if (instruction instanceof And) {
                buildBinaryInstruction(instruction, mipsFunction, mipsBasicBlock, MIPSBinaryType.and);
            } else if (instruction instanceof Icmp) {
                // TODO
            } else if (instruction instanceof Mul) {
                buildBinaryInstruction(instruction, mipsFunction, mipsBasicBlock, MIPSBinaryType.mulu);
            } else if (instruction instanceof Or) {
                buildBinaryInstruction(instruction, mipsFunction, mipsBasicBlock, MIPSBinaryType.or);
            } else if (instruction instanceof Sdiv) {
                if (instruction.getUsedValue(0) instanceof ConstantInt) {
                    int val1 = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                    MIPSli mipsLi1 = new MIPSli(new Reg(8), new Imm(val1));
                    mipsBasicBlock.addMipsInstruction(mipsLi1);
                } else {
                    String op1 = instruction.getUsedValue(0).getName().substring(1);
                    int fpOffset1 = mipsFunction.getNameToFp().get(op1);
                    MIPSlw mipsLw1 = new MIPSlw(new Reg(8), new Imm(fpOffset1), new Reg(30));
                    mipsBasicBlock.addMipsInstruction(mipsLw1);
                }

                if (instruction.getUsedValue(1) instanceof ConstantInt) {
                    int val2 = ((ConstantInt) instruction.getUsedValue(1)).getVal();
                    MIPSli mipsLi2 = new MIPSli(new Reg(9), new Imm(val2));
                    mipsBasicBlock.addMipsInstruction(mipsLi2);
                } else {
                    String op2 = instruction.getUsedValue(1).getName().substring(1);
                    int fpOffset2 = mipsFunction.getNameToFp().get(op2);
                    MIPSlw mipsLw2 = new MIPSlw(new Reg(9), new Imm(fpOffset2), new Reg(30));
                    mipsBasicBlock.addMipsInstruction(mipsLw2);
                }

                MIPSdivu mipsDivu = new MIPSdivu(new Reg(8), new Reg(9));
                mipsBasicBlock.addMipsInstruction(mipsDivu);
                MIPSmflo mipsMflo = new MIPSmflo(new Reg(10));
                mipsBasicBlock.addMipsInstruction(mipsMflo);

                int functionFPOffset = mipsFunction.getFunctionFP();
                mipsFunction.addFunctionFP(4);
                MIPSsw mipsSw = new MIPSsw(new Reg(10), new Imm(functionFPOffset), new Reg(30));
                mipsBasicBlock.addMipsInstruction(mipsSw);
                mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
            } else if (instruction instanceof Srem) {
                if (instruction.getUsedValue(0) instanceof ConstantInt) {
                    int val1 = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                    MIPSli mipsLi1 = new MIPSli(new Reg(8), new Imm(val1));
                    mipsBasicBlock.addMipsInstruction(mipsLi1);
                } else {
                    String op1 = instruction.getUsedValue(0).getName().substring(1);
                    int fpOffset1 = mipsFunction.getNameToFp().get(op1);
                    MIPSlw mipsLw1 = new MIPSlw(new Reg(8), new Imm(fpOffset1), new Reg(30));
                    mipsBasicBlock.addMipsInstruction(mipsLw1);
                }

                if (instruction.getUsedValue(1) instanceof ConstantInt) {
                    int val2 = ((ConstantInt) instruction.getUsedValue(1)).getVal();
                    MIPSli mipsLi2 = new MIPSli(new Reg(9), new Imm(val2));
                    mipsBasicBlock.addMipsInstruction(mipsLi2);
                } else {
                    String op2 = instruction.getUsedValue(1).getName().substring(1);
                    int fpOffset2 = mipsFunction.getNameToFp().get(op2);
                    MIPSlw mipsLw2 = new MIPSlw(new Reg(9), new Imm(fpOffset2), new Reg(30));
                    mipsBasicBlock.addMipsInstruction(mipsLw2);
                }

                MIPSdivu mipsDivu = new MIPSdivu(new Reg(8), new Reg(9));
                mipsBasicBlock.addMipsInstruction(mipsDivu);
                MIPSmfhi mipsMfhi = new MIPSmfhi(new Reg(10));
                mipsBasicBlock.addMipsInstruction(mipsMfhi);

                int functionFPOffset = mipsFunction.getFunctionFP();
                mipsFunction.addFunctionFP(4);
                MIPSsw mipsSw = new MIPSsw(new Reg(10), new Imm(functionFPOffset), new Reg(30));
                mipsBasicBlock.addMipsInstruction(mipsSw);
                mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
            } else if (instruction instanceof Sub) {
                buildBinaryInstruction(instruction, mipsFunction, mipsBasicBlock, MIPSBinaryType.subu);
            } else if (instruction instanceof Alloca) {
                if (((PointerType) instruction.getType()).getPointToType() instanceof IntegerType) {
                    mipsFunction.addPointToFp(instruction.getName().substring(1), mipsFunction.getFunctionFP());
                    mipsFunction.addFunctionFP(4);
                }
            } else if (instruction instanceof Getelementptr) {
                // TODO
            } else if (instruction instanceof Load) {
                String op = instruction.getUsedValue(0).getName().substring(1);
                if (mipsFunction.getPointToFp().containsKey(op)) {
                    int fpOffset = mipsFunction.getPointToFp().get(op);

                    int functionFPOffset = mipsFunction.getFunctionFP();
                    mipsFunction.addFunctionFP(4);
                    MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                    MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(functionFPOffset), new Reg(30));
                    mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);

                    mipsBasicBlock.addMipsInstruction(mipsLw);
                    mipsBasicBlock.addMipsInstruction(mipsSw);
                } else {
                    int functionFPOffset = mipsFunction.getFunctionFP();
                    mipsFunction.addFunctionFP(4);
                    MIPSlw mipsLw = new MIPSlw(new Reg(8), new Label(op));
                    MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(functionFPOffset), new Reg(30));
                    mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);

                    mipsBasicBlock.addMipsInstruction(mipsLw);
                    mipsBasicBlock.addMipsInstruction(mipsSw);
                }

            } else if (instruction instanceof Phi) {
                // TODO
            } else if (instruction instanceof Store) {
                if (instruction.getUsedValue(0) instanceof ConstantInt) {
                    int val1 = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                    MIPSli mipsLi1 = new MIPSli(new Reg(8), new Imm(val1));
                    mipsBasicBlock.addMipsInstruction(mipsLi1);

                    String op2 = instruction.getUsedValue(1).getName().substring(1);
                    if (mipsFunction.getPointToFp().containsKey(op2)) {
                        int fpOffset2 = mipsFunction.getPointToFp().get(op2);
                        MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(fpOffset2), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    } else {
                        MIPSsw mipsSw = new MIPSsw(new Reg(8), new Label(op2));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }
                } else {
                    String op1 = instruction.getUsedValue(0).getName().substring(1);
                    int fpOffset1 = mipsFunction.getNameToFp().get(op1);
                    MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset1), new Reg(30));
                    mipsBasicBlock.addMipsInstruction(mipsLw);

                    String op2 = instruction.getUsedValue(1).getName().substring(1);
                    if (mipsFunction.getPointToFp().containsKey(op2)) {
                        int fpOffset2 = mipsFunction.getPointToFp().get(op2);
                        MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(fpOffset2), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    } else {
                        MIPSsw mipsSw = new MIPSsw(new Reg(8), new Label(op2));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }
                }
            } else if (instruction instanceof ZextTo) {
                // TODO
            } else if (instruction instanceof Br) {
                // TODO
            } else if (instruction instanceof Call) {
                if (((Function) instruction.getUsedValue(0)).isBuiltIn()) {
                    if (instruction.getUsedValue(0).getName().equals("@getint")) {
                        MIPSli mipsLi = new MIPSli(new Reg(2), new Imm(5));
                        MIPSsyscall mipsSyscall = new MIPSsyscall();
                        mipsBasicBlock.addMipsInstruction(mipsLi);
                        mipsBasicBlock.addMipsInstruction(mipsSyscall);

                        int functionFPOffset = mipsFunction.getFunctionFP();
                        mipsFunction.addFunctionFP(4);
                        MIPSsw mipsSw = new MIPSsw(new Reg(2), new Imm(functionFPOffset), new Reg(30));
                        mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);

                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    } else if (instruction.getUsedValue(0).getName().equals("@putch")) {
                        MIPSli mipsLi = new MIPSli(new Reg(2), new Imm(11));
                        mipsBasicBlock.addMipsInstruction(mipsLi);

                        mipsLi = new MIPSli(new Reg(4), new Imm(((ConstantInt) instruction.getUsedValue(1)).getVal()));
                        mipsBasicBlock.addMipsInstruction(mipsLi);

                        MIPSsyscall mipsSyscall = new MIPSsyscall();
                        mipsBasicBlock.addMipsInstruction(mipsSyscall);
                    } else if (instruction.getUsedValue(0).getName().equals("@putint")) {
                        MIPSli mipsLi = new MIPSli(new Reg(2), new Imm(1));
                        mipsBasicBlock.addMipsInstruction(mipsLi);

                        if (instruction.getUsedValue(1) instanceof ConstantInt) {
                            int val = ((ConstantInt) instruction.getUsedValue(1)).getVal();
                            mipsLi = new MIPSli(new Reg(4), new Imm(val));
                            mipsBasicBlock.addMipsInstruction(mipsLi);
                        } else {
                            String op1 = instruction.getUsedValue(1).getName().substring(1);
                            int fpOffset = mipsFunction.getNameToFp().get(op1);
                            MIPSlw mipsLw = new MIPSlw(new Reg(4), new Imm(fpOffset), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsLw);
                        }

                        MIPSsyscall mipsSyscall = new MIPSsyscall();
                        mipsBasicBlock.addMipsInstruction(mipsSyscall);
                    } else {
                        // TODO @putstr
                    }
                } else {
                    // 压栈
                    MIPSBinaryI mipsBinaryI = new MIPSBinaryI(MIPSBinaryIType.addi,
                            new Reg(29), new Reg(29), new Imm(-4));
                    mipsBasicBlock.addMipsInstruction(mipsBinaryI);
                    // 保存现场
                    MIPSsw mipsSw = new MIPSsw(new Reg(31), new Imm(0), new Reg(29));
                    mipsBasicBlock.addMipsInstruction(mipsSw);

                    // 传入参数
                    int functionFPOffset = mipsFunction.getFunctionFP();
                    int argCnt = ((Function) instruction.getUsedValue(0)).getArguments().size();
                    for (int i = 1; i <= argCnt; i++) {
                        if (instruction.getUsedValue(i) instanceof ConstantInt) {
                            int val = ((ConstantInt) instruction.getUsedValue(i)).getVal();
                            MIPSli mipsLi = new MIPSli(new Reg(8), new Imm(val));
                            mipsBasicBlock.addMipsInstruction(mipsLi);
                        } else {
                            String op1 = instruction.getUsedValue(i).getName().substring(1);
                            int fpOffset = mipsFunction.getNameToFp().get(op1);
                            MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsLw);
                        }
                        mipsSw = new MIPSsw(new Reg(8), new Imm(functionFPOffset + 4 * (i - 1)), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }

                    // 移动帧指针
                    mipsBinaryI = new MIPSBinaryI(MIPSBinaryIType.addi,
                            new Reg(30), new Reg(30), new Imm(functionFPOffset));
                    mipsBasicBlock.addMipsInstruction(mipsBinaryI);

                    // 调用函数
                    MIPSjal mipsJal = new MIPSjal(new Label(instruction.getUsedValue(0).getName().substring(1)));
                    mipsBasicBlock.addMipsInstruction(mipsJal);

                    // 移动帧指针
                    mipsBinaryI = new MIPSBinaryI(MIPSBinaryIType.addi,
                            new Reg(30), new Reg(30), new Imm(-1 * functionFPOffset));
                    mipsBasicBlock.addMipsInstruction(mipsBinaryI);

                    // 恢复现场
                    MIPSlw mipsLw = new MIPSlw(new Reg(31), new Imm(0), new Reg(29));
                    mipsBasicBlock.addMipsInstruction(mipsLw);
                    // 弹栈
                    mipsBinaryI = new MIPSBinaryI(MIPSBinaryIType.addi,
                            new Reg(29), new Reg(29), new Imm(4));
                    mipsBasicBlock.addMipsInstruction(mipsBinaryI);

                    if (((Function) instruction.getUsedValue(0)).getReturnType() instanceof IntegerType) {
                        functionFPOffset = mipsFunction.getFunctionFP();
                        mipsFunction.addFunctionFP(4);
                        mipsSw = new MIPSsw(new Reg(2), new Imm(functionFPOffset), new Reg(30));
                        mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);

                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }

                }
            } else if (instruction instanceof Ret) {
                if (!((Ret) instruction).isVoid()) {
                    if (instruction.getUsedValue(0) instanceof ConstantInt) {
                        int val = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                        MIPSli mipsLi = new MIPSli(new Reg(2), new Imm(val));
                        mipsBasicBlock.addMipsInstruction(mipsLi);
                    } else {
                        String op1 = instruction.getUsedValue(0).getName().substring(1);
                        int fpOffset = mipsFunction.getNameToFp().get(op1);
                        MIPSlw mipsLw = new MIPSlw(new Reg(2), new Imm(fpOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw);
                    }
                }
            }
        }

    }

    private void buildBinaryInstruction(Instruction instruction, MipsFunction mipsFunction,
                                        MipsBasicBlock mipsBasicBlock, MIPSBinaryType type) {
        if (instruction.getUsedValue(0) instanceof ConstantInt) {
            int val1 = ((ConstantInt) instruction.getUsedValue(0)).getVal();
            MIPSli mipsLi1 = new MIPSli(new Reg(8), new Imm(val1));
            mipsBasicBlock.addMipsInstruction(mipsLi1);
        } else {
            String op1 = instruction.getUsedValue(0).getName().substring(1);
            int fpOffset1 = mipsFunction.getNameToFp().get(op1);
            MIPSlw mipsLw1 = new MIPSlw(new Reg(8), new Imm(fpOffset1), new Reg(30));
            mipsBasicBlock.addMipsInstruction(mipsLw1);
        }

        if (instruction.getUsedValue(1) instanceof ConstantInt) {
            int val2 = ((ConstantInt) instruction.getUsedValue(1)).getVal();
            MIPSli mipsLi2 = new MIPSli(new Reg(9), new Imm(val2));
            mipsBasicBlock.addMipsInstruction(mipsLi2);
        } else {
            String op2 = instruction.getUsedValue(1).getName().substring(1);
            int fpOffset2 = mipsFunction.getNameToFp().get(op2);
            MIPSlw mipsLw2 = new MIPSlw(new Reg(9), new Imm(fpOffset2), new Reg(30));
            mipsBasicBlock.addMipsInstruction(mipsLw2);
        }

        MIPSBinary mipsBinary = new MIPSBinary(type, new Reg(10), new Reg(8), new Reg(9));
        mipsBasicBlock.addMipsInstruction(mipsBinary);

        int functionFPOffset = mipsFunction.getFunctionFP();
        mipsFunction.addFunctionFP(4);
        MIPSsw mipsSw = new MIPSsw(new Reg(10), new Imm(functionFPOffset), new Reg(30));
        mipsBasicBlock.addMipsInstruction(mipsSw);
        mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
    }

}
