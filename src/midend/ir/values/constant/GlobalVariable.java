package midend.ir.values.constant;

import midend.ir.types.ArrayType;
import midend.ir.types.IntegerType;
import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.values.Module;

import java.util.ArrayList;

public class GlobalVariable extends Constant {

    // 全局变量和全局数组 (不能用变量初始化)
    // 全局字符串

    private boolean isConst;
    private boolean isInit;
    private static int stringId = 0;

    // 没初值
    // 操作数：默认缺省初值为零或者零数组
    public GlobalVariable(String name, Type type) {
        super("@" + name, new PointerType(type), Module.getInstance(), Constant.getZeroConstant(type));
        isInit = false;
        isConst = false;
    }

    // 有初值
    // 操作数：初始值
    public GlobalVariable(String name, Constant initVal, boolean isConst) {
        super("@" + name, new PointerType(initVal.getType()), Module.getInstance(), initVal);
        isInit = true;
        this.isConst = isConst;
    }

    // 字符串常量
    public GlobalVariable(String str) {
        super("@str_" + stringId, new PointerType(new ArrayType(new IntegerType(8), str.length() + 1)), Module.getInstance(), new ConstantString(str));
        stringId++;
        isInit = true;
        isConst = true;
    }

    public Constant getVal() {
        return ((Constant) this.getUsedValue(0));
    }

    @Override
    public String toString() {
        return this.getName()
//                + " = dso_local "
                + " = "
                + (isConst ? "constant" : "global") + " "
                + ((PointerType) this.getType()).getPointToType().toString() + " "
                + getUsedValue(0).toString();
    }
}
