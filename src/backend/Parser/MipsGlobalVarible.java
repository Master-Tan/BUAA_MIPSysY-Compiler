package backend.Parser;

import midend.ir.types.ArrayType;
import midend.ir.types.IntegerType;
import midend.ir.types.PointerType;
import midend.ir.types.Type;
import midend.ir.values.Value;
import midend.ir.values.constant.Constant;
import midend.ir.values.constant.ConstantArray;
import midend.ir.values.constant.ConstantInt;
import midend.ir.values.constant.ConstantString;
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
        } else if (type instanceof ArrayType) {
            if (globalVariable.isString()) {
                sb.append("\t.asciiz\t");
                sb.append("\"");
                sb.append(((ConstantString) globalVariable.getVal()).getString());
                sb.append("\"");
                sb.append("\n");
            } else if (globalVariable.isInit()) {
                sb.append("\t.word\t");
                for (Integer integer : ((ConstantArray) globalVariable.getVal()).getVal()) {
                    sb.append(integer);
                    sb.append(", ");
                }
                if (!((ConstantArray) globalVariable.getVal()).getVal().isEmpty()) {
                    sb.delete(sb.length() - 2, sb.length());
                }
                sb.append("\n");
            } else {
                sb.append("\t.space\t");
                sb.append(type.getSize());
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
