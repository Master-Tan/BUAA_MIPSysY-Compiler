package backend;

import backend.Instructions.*;
import backend.Parser.*;
import backend.operand.*;
import midend.ir.types.*;
import midend.ir.values.*;
import midend.ir.values.constant.*;
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


                IcmpType icmpType = ((Icmp) instruction).getIcmpType();
                if (icmpType == IcmpType.EQ) {
                    MIPSIcmp mipsIcmp = new MIPSIcmp(MIPSIcmp.MIPSIcmpType.SEQ, new Reg(10), new Reg(8), new Reg(9));
                    mipsBasicBlock.addMipsInstruction(mipsIcmp);
                } else if (icmpType == IcmpType.NE) {
                    MIPSIcmp mipsIcmp = new MIPSIcmp(MIPSIcmp.MIPSIcmpType.SNE, new Reg(10), new Reg(8), new Reg(9));
                    mipsBasicBlock.addMipsInstruction(mipsIcmp);
                } else if (icmpType == IcmpType.SGE) {
                    MIPSIcmp mipsIcmp = new MIPSIcmp(MIPSIcmp.MIPSIcmpType.SGE, new Reg(10), new Reg(8), new Reg(9));
                    mipsBasicBlock.addMipsInstruction(mipsIcmp);
                } else if (icmpType == IcmpType.SGT) {
                    MIPSIcmp mipsIcmp = new MIPSIcmp(MIPSIcmp.MIPSIcmpType.SGT, new Reg(10), new Reg(8), new Reg(9));
                    mipsBasicBlock.addMipsInstruction(mipsIcmp);
                } else if (icmpType == IcmpType.SLE) {
                    MIPSIcmp mipsIcmp = new MIPSIcmp(MIPSIcmp.MIPSIcmpType.SLE, new Reg(10), new Reg(8), new Reg(9));
                    mipsBasicBlock.addMipsInstruction(mipsIcmp);
                } else if (icmpType == IcmpType.SLT) {
                    MIPSIcmp mipsIcmp = new MIPSIcmp(MIPSIcmp.MIPSIcmpType.SLT, new Reg(10), new Reg(8), new Reg(9));
                    mipsBasicBlock.addMipsInstruction(mipsIcmp);
                }

                int functionFPOffset = mipsFunction.getFunctionFP();
                mipsFunction.addFunctionFP(4);
                MIPSsw mipsSw = new MIPSsw(new Reg(10), new Imm(functionFPOffset), new Reg(30));
                mipsBasicBlock.addMipsInstruction(mipsSw);
                mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
            } else if (instruction instanceof Mul) {
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

                MIPSmulu mipsMulu = new MIPSmulu(new Reg(10), new Reg(8), new Reg(9));
                mipsBasicBlock.addMipsInstruction(mipsMulu);

                int functionFPOffset = mipsFunction.getFunctionFP();
                mipsFunction.addFunctionFP(4);
                MIPSsw mipsSw = new MIPSsw(new Reg(10), new Imm(functionFPOffset), new Reg(30));
                mipsBasicBlock.addMipsInstruction(mipsSw);
                mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
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

                MIPSdiv mipsDivu = new MIPSdiv(new Reg(8), new Reg(9));
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

                MIPSdiv mipsDivu = new MIPSdiv(new Reg(8), new Reg(9));
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
                } else if (((PointerType) instruction.getType()).getPointToType() instanceof ArrayType){  // array alloca
                    mipsFunction.addPointToFp(instruction.getName().substring(1), mipsFunction.getFunctionFP());
                    mipsFunction.addFunctionFP(((PointerType) instruction.getType()).getPointToType().getSize());
                } else {  // pointer alloca
                    mipsFunction.addPointToFp(instruction.getName().substring(1), mipsFunction.getFunctionFP());
                    mipsFunction.addIsArrayIndex(instruction.getName().substring(1));
                    mipsFunction.addFunctionFP(((PointerType) instruction.getType()).getPointToType().getSize());
                }
            } else if (instruction instanceof Getelementptr) {
                String base = instruction.getUsedValue(0).getName().substring(1);
                if (mipsFunction.getPointToFp().containsKey(base)) {
                    int fpOffset = mipsFunction.getPointToFp().get(base);

                    if (mipsFunction.getIsArrayIndex().contains(base)) {
                        MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw);
                    } else {
                        MIPSli mipsLi = new MIPSli(new Reg(8), new Imm(fpOffset));
                        mipsBasicBlock.addMipsInstruction(mipsLi);
                    }


                    if (instruction.getUsedValue(1) instanceof ConstantInt) {
                        int val1 = ((ConstantInt) instruction.getUsedValue(1)).getVal();
                        MIPSli mipsLi1 = new MIPSli(new Reg(9), new Imm(val1));
                        mipsBasicBlock.addMipsInstruction(mipsLi1);
                    } else {
                        String op1 = instruction.getUsedValue(1).getName().substring(1);
                        int fpOffset1 = mipsFunction.getNameToFp().get(op1);
                        MIPSlw mipsLw1 = new MIPSlw(new Reg(9), new Imm(fpOffset1), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw1);
                    }

                    MIPSmulu mipsMulu = new MIPSmulu(new Reg(10), new Reg(9), new Imm(((PointerType) instruction.getUsedValue(0).getType()).getPointToType().getSize()));
                    mipsBasicBlock.addMipsInstruction(mipsMulu);

                    MIPSBinary mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(11), new Reg(8), new Reg(10));
                    mipsBasicBlock.addMipsInstruction(mipsBinary);

                    if (instruction.getValueNum() == 3) {
                        if (instruction.getUsedValue(2) instanceof ConstantInt) {
                            int val2 = ((ConstantInt) instruction.getUsedValue(2)).getVal();
                            MIPSli mipsLi2 = new MIPSli(new Reg(12), new Imm(val2));
                            mipsBasicBlock.addMipsInstruction(mipsLi2);
                        } else {
                            String op2 = instruction.getUsedValue(2).getName().substring(1);
                            int fpOffset2 = mipsFunction.getNameToFp().get(op2);
                            MIPSlw mipsLw2 = new MIPSlw(new Reg(12), new Imm(fpOffset2), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsLw2);
                        }
                        mipsMulu = new MIPSmulu(new Reg(13), new Reg(12), new Imm(((ArrayType) ((PointerType) instruction.getUsedValue(0).getType()).getPointToType()).getElementType().getSize()));
                        mipsBasicBlock.addMipsInstruction(mipsMulu);

                        mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(14), new Reg(11), new Reg(13));
                        mipsBasicBlock.addMipsInstruction(mipsBinary);

                    }

                    int functionFPOffset = mipsFunction.getFunctionFP();
                    mipsFunction.addFunctionFP(4);
                    if (instruction.getValueNum() == 3) {
                        MIPSsw mipsSw = new MIPSsw(new Reg(14), new Imm(functionFPOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    } else {
                        MIPSsw mipsSw = new MIPSsw(new Reg(11), new Imm(functionFPOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }

                    mipsFunction.addPointToFp(instruction.getName().substring(1), functionFPOffset);
                    mipsFunction.addIsArrayIndex(instruction.getName().substring(1));  // TODO 可以转为直接存地址，而不是存对当前fp的偏移量
                } else {
                    MIPSla mipsLa = new MIPSla(new Reg(8), new Label(instruction.getUsedValue(0).getName().substring(1)));
                        mipsBasicBlock.addMipsInstruction(mipsLa);

                    if (instruction.getUsedValue(1) instanceof ConstantInt) {
                        int val1 = ((ConstantInt) instruction.getUsedValue(1)).getVal();
                        MIPSli mipsLi1 = new MIPSli(new Reg(9), new Imm(val1));
                        mipsBasicBlock.addMipsInstruction(mipsLi1);
                    } else {
                        String op1 = instruction.getUsedValue(1).getName().substring(1);
                        int fpOffset1 = mipsFunction.getNameToFp().get(op1);
                        MIPSlw mipsLw1 = new MIPSlw(new Reg(9), new Imm(fpOffset1), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw1);
                    }

                    MIPSmulu mipsMulu = new MIPSmulu(new Reg(10), new Reg(9), new Imm(((PointerType) instruction.getUsedValue(0).getType()).getPointToType().getSize()));
                    mipsBasicBlock.addMipsInstruction(mipsMulu);

                    MIPSBinary mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(11), new Reg(8), new Reg(10));
                    mipsBasicBlock.addMipsInstruction(mipsBinary);
                    if (instruction.getValueNum() == 3) {
                        if (instruction.getUsedValue(2) instanceof ConstantInt) {
                            int val2 = ((ConstantInt) instruction.getUsedValue(2)).getVal();
                            MIPSli mipsLi2 = new MIPSli(new Reg(12), new Imm(val2));
                            mipsBasicBlock.addMipsInstruction(mipsLi2);
                        } else {
                            String op2 = instruction.getUsedValue(2).getName().substring(1);
                            int fpOffset2 = mipsFunction.getNameToFp().get(op2);
                            MIPSlw mipsLw2 = new MIPSlw(new Reg(12), new Imm(fpOffset2), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsLw2);
                        }
                        mipsMulu = new MIPSmulu(new Reg(13), new Reg(12), new Imm(((ArrayType) ((PointerType) instruction.getUsedValue(0).getType()).getPointToType()).getElementType().getSize()));
                        mipsBasicBlock.addMipsInstruction(mipsMulu);

                        mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(14), new Reg(11), new Reg(13));
                        mipsBasicBlock.addMipsInstruction(mipsBinary);

                    }

                    int functionFPOffset = mipsFunction.getFunctionFP();
                    mipsFunction.addFunctionFP(4);
                    if (instruction.getValueNum() == 3) {
                        mipsBinary = new MIPSBinary(MIPSBinaryType.subu, new Reg(15), new Reg(14), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsBinary);
                        MIPSsw mipsSw = new MIPSsw(new Reg(15), new Imm(functionFPOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    } else {
                        mipsBinary = new MIPSBinary(MIPSBinaryType.subu, new Reg(15), new Reg(11), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsBinary);
                        MIPSsw mipsSw = new MIPSsw(new Reg(15), new Imm(functionFPOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }

                    mipsFunction.addPointToFp(instruction.getName().substring(1), functionFPOffset);
                    mipsFunction.addIsArrayIndex(instruction.getName().substring(1));
                }
            } else if (instruction instanceof Load) {
                String op = instruction.getUsedValue(0).getName().substring(1);
                if (mipsFunction.getPointToFp().containsKey(op)) {
                    int fpOffset = mipsFunction.getPointToFp().get(op);

                    if (mipsFunction.getIsArrayIndex().contains(op) && !(((Load) instruction).getType() instanceof PointerType)) {
                        MIPSlw mipsLw = new MIPSlw(new Reg(9), new Imm(fpOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw);
                        MIPSBinary mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(10), new Reg(9), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsBinary);
                        mipsLw = new MIPSlw(new Reg(8), new Imm(0), new Reg(10));
                        mipsBasicBlock.addMipsInstruction(mipsLw);
                    } else {
                        MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw);
                    }

                    int functionFPOffset = mipsFunction.getFunctionFP();
                    mipsFunction.addFunctionFP(4);
                    MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(functionFPOffset), new Reg(30));
                    if (((Load) instruction).getType() instanceof PointerType) {
                        mipsFunction.addPointToFp(instruction.getName().substring(1), functionFPOffset);
                        mipsFunction.addIsArrayIndex(instruction.getName().substring(1));
                    } else {
                        mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
                    }
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
                // TODO Phi
            } else if (instruction instanceof Store) {
                if (instruction.getUsedValue(0) instanceof ConstantInt) {
                    int val = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                    MIPSli mipsLi = new MIPSli(new Reg(8), new Imm(val));
                    mipsBasicBlock.addMipsInstruction(mipsLi);

                    String op = instruction.getUsedValue(1).getName().substring(1);
                    if (mipsFunction.getPointToFp().containsKey(op)) {
                        int fpOffset = mipsFunction.getPointToFp().get(op);
                        if (mipsFunction.getIsArrayIndex().contains(op)) {
                            MIPSlw mipsLw = new MIPSlw(new Reg(9), new Imm(fpOffset), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsLw);
                            MIPSBinary mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(10), new Reg(9), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsBinary);
                            MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(0), new Reg(10));
                            mipsBasicBlock.addMipsInstruction(mipsSw);
                        } else {
                            MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(fpOffset), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsSw);
                        }
                    } else {
                        MIPSsw mipsSw = new MIPSsw(new Reg(8), new Label(op));
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
                        if (mipsFunction.getIsArrayIndex().contains(op2) && !mipsFunction.getIsArrayIndex().contains(op1)) {
                            mipsLw = new MIPSlw(new Reg(9), new Imm(fpOffset2), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsLw);
                            MIPSBinary mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(10), new Reg(9), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsBinary);
                            MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(0), new Reg(10));
                            mipsBasicBlock.addMipsInstruction(mipsSw);
                        } else {
                            MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(fpOffset2), new Reg(30));
                            mipsBasicBlock.addMipsInstruction(mipsSw);
                        }
                    } else {
                        MIPSsw mipsSw = new MIPSsw(new Reg(8), new Label(op2));
                        mipsBasicBlock.addMipsInstruction(mipsSw);
                    }

                }
            } else if (instruction instanceof ZextTo) {
                if (instruction.getUsedValue(0) instanceof ConstantInt) {
                    int val = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                    MIPSli mipsLi = new MIPSli(new Reg(8), new Imm(val));
                    mipsBasicBlock.addMipsInstruction(mipsLi);
                } else {
                    String op = instruction.getUsedValue(0).getName().substring(1);
                    int fpOffset = mipsFunction.getNameToFp().get(op);
                    MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                    mipsBasicBlock.addMipsInstruction(mipsLw);
                }

                int functionFPOffset = mipsFunction.getFunctionFP();
                mipsFunction.addFunctionFP(4);
                MIPSsw mipsSw = new MIPSsw(new Reg(8), new Imm(functionFPOffset), new Reg(30));
                mipsBasicBlock.addMipsInstruction(mipsSw);
                mipsFunction.addNameToFp(instruction.getName().substring(1), functionFPOffset);
            } else if (instruction instanceof Br) {
                if (((Br) instruction).isCondition()) {
                    if (instruction.getUsedValue(0) instanceof ConstantInt) {
                        int val = ((ConstantInt) instruction.getUsedValue(0)).getVal();
                        MIPSli mipsLi = new MIPSli(new Reg(8), new Imm(val));
                        mipsBasicBlock.addMipsInstruction(mipsLi);
                    } else {
                        String op = instruction.getUsedValue(0).getName().substring(1);
                        int fpOffset = mipsFunction.getNameToFp().get(op);
                        MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                        mipsBasicBlock.addMipsInstruction(mipsLw);
                    }
                    MIPSbeq mipsBeq = new MIPSbeq(new Reg(8), new Imm(1), new Label(instruction.getUsedValue(1).getName().substring(1)));
                    mipsBasicBlock.addMipsInstruction(mipsBeq);
                    MIPSj mipsJ = new MIPSj(new Label(instruction.getUsedValue(2).getName().substring(1)));
                    mipsBasicBlock.addMipsInstruction(mipsJ);
                } else {
                    MIPSj mipsJ = new MIPSj(new Label(instruction.getUsedValue(0).getName().substring(1)));
                    mipsBasicBlock.addMipsInstruction(mipsJ);
                }
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
                        MIPSli mipsLi = new MIPSli(new Reg(2), new Imm(4));
                        mipsBasicBlock.addMipsInstruction(mipsLi);
                        String op = instruction.getUsedValue(1).getName().substring(1);
                        if (mipsFunction.getPointToFp().containsKey(op)) {
                            int fpOffset = mipsFunction.getPointToFp().get(op);

                            if (mipsFunction.getIsArrayIndex().contains(op)) {
                                MIPSlw mipsLw = new MIPSlw(new Reg(9), new Imm(fpOffset), new Reg(30));
                                mipsBasicBlock.addMipsInstruction(mipsLw);
                                MIPSBinary mipsBinary = new MIPSBinary(MIPSBinaryType.addu, new Reg(4), new Reg(9), new Reg(30));
                                mipsBasicBlock.addMipsInstruction(mipsBinary);
                            } else {
                                MIPSlw mipsLw = new MIPSlw(new Reg(4), new Imm(fpOffset), new Reg(30));
                                mipsBasicBlock.addMipsInstruction(mipsLw);  // maybe wrong
                            }
                        } else {
                            MIPSla mipsLa = new MIPSla(new Reg(4), new Label(op));
                            mipsBasicBlock.addMipsInstruction(mipsLa);
                        }

                        MIPSsyscall mipsSyscall = new MIPSsyscall();
                        mipsBasicBlock.addMipsInstruction(mipsSyscall);
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
                            String op = instruction.getUsedValue(i).getName().substring(1);
                            if (mipsFunction.getNameToFp().containsKey(op)) {
                                int fpOffset = mipsFunction.getNameToFp().get(op);
                                MIPSlw mipsLw = new MIPSlw(new Reg(8), new Imm(fpOffset), new Reg(30));
                                mipsBasicBlock.addMipsInstruction(mipsLw);
                            } else {
                                int fpOffset = mipsFunction.getPointToFp().get(op);

                                if (mipsFunction.getIsArrayIndex().contains(op)) {
                                    MIPSlw mipsLw = new MIPSlw(new Reg(9), new Imm(fpOffset), new Reg(30));
                                    mipsBasicBlock.addMipsInstruction(mipsLw);
                                    mipsBinaryI = new MIPSBinaryI(MIPSBinaryIType.addi, new Reg(8), new Reg(9), new Imm(-1 * functionFPOffset));
                                    mipsBasicBlock.addMipsInstruction(mipsBinaryI);
                                } else {
                                    MIPSli mipsLi = new MIPSli(new Reg(8), new Imm(fpOffset));
                                    mipsBasicBlock.addMipsInstruction(mipsLi);
                                }
                            }
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
                    if (!mipsFunction.getName().equals("main")) {
                        MIPSjr mipsJr = new MIPSjr(new Reg(31));
                        mipsBasicBlock.addMipsInstruction(mipsJr);
                    } else {
                        MIPSli mipsLi = new MIPSli(new Reg(2), new Imm(10));
                        mipsBasicBlock.addMipsInstruction(mipsLi);
                        MIPSsyscall mipsSyscall = new MIPSsyscall();
                        mipsBasicBlock.addMipsInstruction(mipsSyscall);

                    }
                } else {
                    MIPSjr mipsJr = new MIPSjr(new Reg(31));
                    mipsBasicBlock.addMipsInstruction(mipsJr);
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
