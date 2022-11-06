package frontend.grammaticalAnalysis;

import frontend.grammaticalAnalysis.grammatical.*;
import frontend.lexicalAnalysis.lexical.Word;
import myclasses.CategoryCodeEnum;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    private ArrayList<HashMap<String, Node>> varMapList;
    private HashMap<String, FuncDefNode> funMap;

    private int index;

    private int loop;

    private boolean isInVoidFun;

    public SymbolTable() {
        varMapList = new ArrayList<HashMap<String, Node>>() {{
            add(new HashMap<>());
        }};
        funMap = new HashMap<>();
        index = 0;
        loop = 0;
        isInVoidFun = false;
    }

    public SymbolTable(ArrayList<HashMap<String, Node>> varMapList, HashMap<String, FuncDefNode> funMap, int index, int loop) {
        this.varMapList = new ArrayList<HashMap<String, Node>>(varMapList);
        this.funMap = funMap;
        this.index = index;
        this.loop = loop;
    }

    public void addFun(Word ident, FuncDefNode node) {
        funMap.put(ident.getWordValue(), node);
    }

    public void addVar(Word ident, Node node) {
        if (!node.checkErrorB(this)) {
            varMapList.get(index).put(ident.getWordValue(), node);
//            System.out.println(this);
        }
    }

    public boolean isConst(Word ident) {
        Node var = findVar(ident);
        if (var == null) {
            return false;
        }
        if (var instanceof ConstDefNode) {
            return true;
        }
        return false;
    }

    public boolean isRedefine(Word ident) {
        return varMapList.get(index).containsKey(ident.getWordValue());
    }

    public boolean isUndefine(Word ident) {
        boolean flag = true;
        for (int i = index; i >= 0; i--) {
            if (varMapList.get(i).containsKey(ident.getWordValue())) {
                flag = false;
            }
        }
        return flag;
    }

    public boolean isParaNumberUnmatch(Word ident, FuncRParamsNode funcRParamsNode) {
        if (funMap.get(ident.getWordValue()).getFuncFParamsNode() == null) {
            if (funcRParamsNode == null) {
                return false;
            } else {
                return true;
            }
        } else if (funcRParamsNode == null) {
            return true;
        } else if (funMap.get(ident.getWordValue()).getFuncFParamsNode().getFuncFParamNodes().size() !=
                funcRParamsNode.getExpNodes().size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isParaTypeUnmatch(Word ident, FuncRParamsNode funcRParamsNode) {
        if (funMap.get(ident.getWordValue()).getFuncFParamsNode() == null) {
            return false;
        } else {
            boolean flag = false;
            for (int i = 0, j = 0; i < funMap.get(ident.getWordValue()).getFuncFParamsNode().getFuncFParamNodes().size() &&
                    j < funcRParamsNode.getExpNodes().size(); i++, j++) {
                Node.DataType type1 = funMap.get(ident.getWordValue()).getFuncFParamsNode().getFuncFParamNodes().get(i).getDataType();
                Node.DataType type2 = funcRParamsNode.getExpNodes().get(j).getDataType(this);
                if (type1 == null || type2 == null) {
                    continue;
                }
                if (type1 != type2) {
                    flag = true;
                }
            }
            return flag;
        }
    }

    public Node.DataType getVarType(Word ident) {
        Node var = findVar(ident);
        if (var == null) {
            return null;
        }
        if (var instanceof MainFuncDefNode) {
            return Node.DataType.INT;
        } else if (var instanceof FuncDefNode) {
            Word reserved = getFunReturnType(ident);
            if (reserved.getCategoryCode() == CategoryCodeEnum.CategoryCode.VOIDTK) {
                return Node.DataType.VOID;
            } else {
                return Node.DataType.INT;
            }
        } else if (var instanceof FuncFParamNode) {
            return ((FuncFParamNode) var).getDataType();
        } else if (var instanceof VarDefNode) {
            return ((VarDefNode) var).getDataType();
        } else {
            return ((ConstDefNode) var).getDataType();
        }
    }

    public Word getFunReturnType(Word ident) {
        return funMap.get(ident.getWordValue()).getFuncTypeNode().getReserved();
    }

    public Node findVar(Word ident) {
        for (int i = index; i >= 0; i--) {
            if (varMapList.get(i).containsKey(ident.getWordValue())) {
                return varMapList.get(i).get(ident.getWordValue());
            }
        }
        return null;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public boolean isInVoidFun() {
        return isInVoidFun;
    }

    public void setInVoidFun(boolean inVoidFun) {
        isInVoidFun = inVoidFun;
    }

    public void toChild() {
        index++;
        varMapList.add(new HashMap<>());
    }

    public void back() {
        varMapList.remove(index);
        index--;
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "varMapList=" + varMapList +
                ", funMap=" + funMap +
                ", index=" + index +
                '}';
    }

    public SymbolTable yield() {
        return new SymbolTable(this.varMapList, this.funMap, this.index, this.loop);
    }
}
