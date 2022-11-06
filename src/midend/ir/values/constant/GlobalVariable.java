package midend.ir.values.constant;

import midend.ir.types.ArrayType;
import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.values.Module;

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
        // TODO 零数组
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
        super("@str_" + stringId, new PointerType(new ArrayType()), Module.getInstance(), new ConstantString(str));
        // TODO
        stringId++;
        isInit = true;
        isConst = true;
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
