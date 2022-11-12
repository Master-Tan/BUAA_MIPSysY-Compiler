package backend.Parser;

import midend.ir.types.ArrayType;
import midend.ir.types.IntegerType;
import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.values.constant.Constant;
import midend.ir.values.constant.ConstantInt;
import midend.ir.values.constant.GlobalVariable;

public class MipsGlobalVarible {

    private String name;
    private GlobalVariable globalVariable;

    public MipsGlobalVarible(String name, GlobalVariable globalVariable) {
        this.name = name;
        this.globalVariable = globalVariable;
    }

    public GlobalVariable getGlobalVariable() {
        return globalVariable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:", this.name));

        Type type = ((PointerType) globalVariable.getType()).getPointToType();
        if (type instanceof IntegerType) {
            sb.append("\t.word\t");
            sb.append(((ConstantInt) globalVariable.getVal()).getVal());
            sb.append("\n");
        }
        else if (type instanceof ArrayType) {
            // TODO
        }

        return sb.toString();
    }
}
