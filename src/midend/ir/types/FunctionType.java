package midend.ir.types;

import java.util.ArrayList;

public class FunctionType extends Type {

    // 记录函数信息

    private ArrayList<Type> args;
    private Type returnType;

    public FunctionType(ArrayList<Type> args, Type returnType) {
        this.args = args;
        this.returnType = returnType;
    }

    public ArrayList<Type> getArgs() {
        return args;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public int getSize() {
        return 0;
    }


}
