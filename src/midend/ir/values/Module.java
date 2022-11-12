package midend.ir.values;

import midend.ir.values.constant.Function;
import midend.ir.values.constant.GlobalVariable;

import java.util.ArrayList;

public class Module extends Value {

    // 顶层模块

    private static final Module module = new Module();

    private ArrayList<Function> functions = new ArrayList<>();
    private ArrayList<GlobalVariable> globalVariables = new ArrayList<>();

    public static Module getInstance() {
        return module;
    }

    public Module() {
        super("module", null, null) ;
    }

    public void addFunction(Function function) {
        this.functions.add(function);
    }

    public void addGlobalVariable(GlobalVariable globalVariable) {
        this.globalVariables.add(globalVariable);
    }

    public Function getFunction(String name) {
        for (Function function : functions) {
            if (function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public ArrayList<GlobalVariable> getGlobalVariables() {
        return globalVariables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (GlobalVariable globalVariable : globalVariables) {
            sb.append(globalVariable.toString());
            sb.append("\n");
            }
        sb.append("\n");
        for (Function function : functions) {
            sb.append(function.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
