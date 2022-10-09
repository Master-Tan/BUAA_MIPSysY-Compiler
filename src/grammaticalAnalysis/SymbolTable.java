package grammaticalAnalysis;

import grammaticalAnalysis.grammatical.*;
import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    private ArrayList<HashMap<String, Node>> varMapList;
    private HashMap<String, FuncDefNode> funMap;

    private int index;

    public SymbolTable() {
        varMapList = new ArrayList<HashMap<String, Node>>() {{
            add(new HashMap<>());
        }};
        funMap = new HashMap<>();
        index = 0;
    }

    public void addFun(String name, FuncDefNode node) {
        funMap.put(name, node);
    }

    public void addVar(String name, Node node) {
        if (!node.checkErrorB(this)) {
            varMapList.get(index).put(name, node);
            if (node instanceof FuncDefNode) {
                this.addFun(((FuncDefNode) node).getIdent().getWordValue(), (FuncDefNode) node);
            }
            if (node instanceof MainFuncDefNode) {
                this.addFun(((MainFuncDefNode) node).getIdent().getWordValue(), (MainFuncDefNode) node);
            }
            System.out.println(this);
        }
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
        if (funMap.get(ident.getWordValue()).getFuncFParamsNode().getFuncFParamNodes().size() !=
                funcRParamsNode.getExpNodes().size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isParaTypeUnmatch(Word ident, FuncRParamsNode funcRParamsNode) {
        boolean flag = false;
        for (int i = 0, j = 0; i < funMap.get(ident.getWordValue()).getFuncFParamsNode().getFuncFParamNodes().size() &&
        j < funcRParamsNode.getExpNodes().size(); i++, j++) {
            // TODO
        }
        return flag;
    }

    public void toChild() {
        index++;
        varMapList.add(new HashMap<>());
    }

    public void back() {
        varMapList.remove(index);
        index--;
    }

    public boolean find(String name) {
        return true;
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "varMapList=" + varMapList +
                ", funMap=" + funMap +
                ", index=" + index +
                '}';
    }
}
