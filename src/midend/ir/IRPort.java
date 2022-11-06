package midend.ir;

import midend.ir.types.*;
import midend.ir.values.*;
import midend.ir.values.constant.*;
import midend.ir.values.instructions.binary.*;
import midend.ir.values.instructions.memory.*;
import midend.ir.values.instructions.terminator.*;

import java.util.ArrayList;

public class IRPort {
    public static int nameNumCounter = 0;

    // Builder

    public static Add buildAdd(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Add ans = new Add("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static And buildAnd(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        And ans = new And("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static Icmp buildIcmp(BasicBlock parent, IcmpType icmpType, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Icmp ans = new Icmp("_" + nameNum, parent, op1, op2, icmpType);
        parent.addInstruction(ans);
        return ans;
    }

    public static Mul buildMul(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Mul ans = new Mul("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static Or buildOr(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Or ans = new Or("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static Sdiv buildSdiv(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Sdiv ans = new Sdiv("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static Srem buildSrem(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Srem ans = new Srem("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static Sub buildSub(BasicBlock parent, Value op1, Value op2) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Sub ans = new Sub("_" + nameNum, parent, op1, op2);
        parent.addInstruction(ans);
        return ans;
    }

    public static Alloca buildAlloca(Type allocatedType, BasicBlock parent) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        BasicBlock realParent = parent.getParent().getBasicBlocks().get(0);
        Alloca ans = new Alloca("_" + nameNum, allocatedType, realParent);
        parent.addInstruction(ans);
        return ans;
    }

    public static Getelementptr buildGetelementptr(BasicBlock parent, Value op1, Value op2) {
        // TODO
        return null;
    }

    public static Load buildLoad(Type type, BasicBlock parent, Value location) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Load ans = new Load("_" + nameNum, type, parent, location);
        parent.addInstruction(ans);
        return ans;
    }

    public static Phi buildPhi(BasicBlock parent, Value op1, Value op2) {
        // TODO
        return null;
    }

    public static Store buildStore(BasicBlock parent, Value storeVal, Value location) {
        Store ans = new Store(parent, storeVal, location);
        parent.addInstruction(ans);
        return ans;
    }

    public static ZextTo buildZextTo(BasicBlock parent, Value zextVal, Type toType) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        ZextTo ans = new ZextTo("_" + nameNum, parent, zextVal, toType);
        parent.addInstruction(ans);
        return ans;
    }

    public static Br buildBrWithCondition(BasicBlock parent, Value condition, BasicBlock trueBlock, BasicBlock falseBlock) {
        Br ans = new Br(parent, condition, trueBlock, falseBlock);
        parent.addInstruction(ans);
        return ans;
    }

    public static Br buildBrNoCondition(BasicBlock parent, BasicBlock toBlock) {
        Br ans = new Br(parent, toBlock);
        parent.addInstruction(ans);
        return ans;
    }

    public static Call buildCallWithReturn(BasicBlock parent, Function function, ArrayList<Value> args) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        Call ans = new Call("_" + nameNum, parent, function, args);
        parent.addInstruction(ans);
        return ans;
    }

    public static Call buildCallNoReturn(BasicBlock parent, Function function, ArrayList<Value> args) {
        Call ans = new Call(parent, function, args);
        parent.addInstruction(ans);
        return ans;
    }

    public static Ret buildRetWithReturn(BasicBlock parent, Value returnVal) {
        Ret ans = new Ret(parent, returnVal);
        parent.addInstruction(ans);
        return ans;
    }

    public static Ret buildRetNoReturn(BasicBlock parent) {
        Ret ans = new Ret(parent);
        parent.addInstruction(ans);
        return ans;
    }

    public static BasicBlock buildBasicBlock(String name, Function parent) {
        int nameNum = nameNumCounter;
        nameNumCounter++;
        BasicBlock ans = new BasicBlock(name + "_" + nameNum, parent);
        parent.addBasicBlock(ans);
        return ans;
    }

    public static Function buildFunction(String name, FunctionType functionType) {
        Function ans = new Function(name, functionType, false);
        Module.getInstance().addFunction(ans);
        nameNumCounter = ans.getArguments().size();
        return ans;
    }

    // Getter

    public static FunctionType getFunctionType(ArrayList<Type> args, Type returnType) {
        return new FunctionType(args, returnType);
    }

    public static VoidType getVoidType() {
        return new VoidType();
    }

    public static IntegerType getIntType(int bits) {
        return new IntegerType(bits);
    }

    public static ConstantInt getConstantInt(int bits, int val) {
        return new ConstantInt(bits, val);
    }

    public static GlobalVariable getGlobalVariable(String name, Type type) {
        return new GlobalVariable(name, type);
    }

    public static GlobalVariable getGlobalVariable(String name, Constant initVal, boolean isConst) {
        return new GlobalVariable(name, initVal, isConst);
    }

}
