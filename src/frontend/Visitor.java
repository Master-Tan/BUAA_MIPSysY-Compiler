package frontend;

import exceptions.SysYException;
import frontend.grammaticalAnalysis.grammatical.*;
import frontend.grammaticalAnalysis.grammatical.Stmt.*;
import frontend.lexicalAnalysis.lexical.Word;
import midend.ir.IRPort;
import midend.ir.types.*;
import midend.ir.values.*;
import midend.ir.values.constant.*;
import midend.ir.values.instructions.binary.IcmpType;
import midend.ir.values.instructions.memory.Alloca;
import midend.ir.values.instructions.memory.Getelementptr;
import midend.ir.values.instructions.memory.Load;
import midend.ir.values.instructions.terminator.Br;
import midend.ir.values.instructions.terminator.Ret;
import myclasses.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Visitor {

    private Function currentFunction;
    private BasicBlock currentBasicBlock;

    private Value currentValue;
    private int currentInt;
    private Type currentType;
    private ArrayList<Value> currentArray;
    private ArrayList<Type> currentTypeArray;
    private BasicBlock currentIfBasicBlock;
    private BasicBlock currentElseBasicBlock;
    private BasicBlock currentBreakBasicBlock;
    private BasicBlock currentContinueBasicBlock;
    private ArrayList<Integer> currentDimensions;
    private int currentArrayIndex;


    private boolean isConstant = false;
    private boolean isGlobalInit = false;
    private boolean isCallingFunc = false;
    private boolean isSingleJudge = false;
    private boolean isArrayInit = false;

    // 存储变量的值
    private ArrayList<HashMap<String, Value>> symbolVal = new ArrayList<HashMap<String, Value>>(){{
        add(new HashMap<>());
    }};
    private int symbolValIndex = 0;
    private boolean isEnterSymbolVal = false;

    public Visitor() {
    }

    /*
        编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef
     */
    public void visitCompUnit(CompUnitNode compUnitNode) {
        ArrayList<DeclNode> declNodes = compUnitNode.getDeclNodes();
        ArrayList<FuncDefNode> funcDefNodes = compUnitNode.getFuncDefNodes();
        MainFuncDefNode mainFuncDefNode = compUnitNode.getMainFuncDefNode();

        for (DeclNode declNode : declNodes) {
            visitDecl(declNode);
        }
        for (FuncDefNode funcDefNode : funcDefNodes) {
            visitFuncDef(funcDefNode);
        }
        visitMainFuncDef(mainFuncDefNode);
    }

    /*
        声明 Decl → ConstDecl | VarDecl
     */
    public void visitDecl(DeclNode declNode) {
        ConstDeclNode constDeclNode = declNode.getConstDeclNode();
        VarDeclNode varDeclNode = declNode.getVarDeclNode();

        if (constDeclNode != null) {
            visitConstDecl(constDeclNode);
        } else {
            visitVarDecl(varDeclNode);
        }
    }

    /*
        常量声明 ConstDecl → 'const' 'int' ConstDef { ',' ConstDef } ';'
     */
    public void visitConstDecl(ConstDeclNode constDeclNode) {
         ArrayList<ConstDefNode> constDefNodes = constDeclNode.getConstDefNodes();

        for (ConstDefNode constDefNode : constDefNodes) {
            visitConstDef(constDefNode);
        }
    }

    /*
        常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
     */
    public void visitConstDef(ConstDefNode constDefNode) {
        Word ident = constDefNode.getIdent();
        ArrayList<ConstExpNode> constExpNodes = constDefNode.getConstExpNodes();
        ConstInitValNode constInitValNode = constDefNode.getConstInitvalNode();

        String varName = ident.getWordValue();
        if (constExpNodes.isEmpty()) {
            visitConstInitVal(constInitValNode);
            symbolVal.get(symbolValIndex).put(varName, currentValue);
        } else {  // array
            ArrayList<Integer> dimensions = new ArrayList<>();

            for (ConstExpNode constExpNode : constExpNodes) {
                isConstant = true;
                visitConstExp(constExpNode);
                isConstant = false;
                dimensions.add(((ConstantInt) currentValue).getVal());
            }

            Type arrayType = new IntegerType(32);

            for (int i = dimensions.size() - 1; i >= 0; i--) {
                arrayType = IRPort.getArrayType(arrayType, dimensions.get(i));
            }

            if (symbolValIndex == 0) {  // 全局变量
                currentDimensions = new ArrayList<>(dimensions);
                isGlobalInit = true;
                isArrayInit = true;
                visitConstInitVal(constInitValNode);
                isArrayInit = false;
                isGlobalInit = false;

                ArrayList<Constant> constantArray = new ArrayList<>();

                // 全零优化
                boolean isZero = true;
                for (Value value : currentArray) {
                    if (((ConstantInt) value).getVal() != 0) {
                        isZero = false;
                    }
                    constantArray.add((ConstantInt) value);
                }
                int arraySize = 1;
                for (Integer dimension : dimensions) {
                    arraySize = arraySize * dimension;
                }
                GlobalVariable globalVariable;
                if (isZero) {
                    globalVariable = IRPort.getZeroGlobalVariable(varName, arrayType);
                } else {
                    // 根据所有的常量值和数组维度信息构建数组常量
                    for (int i = dimensions.size() - 1; i > 0; i--) {
                        int nowDimension = dimensions.get(i);
                        ArrayList<Constant> nowConstantArray = new ArrayList<>();
                        arraySize = arraySize / nowDimension;
                        for (int j = 0; j < arraySize; j++) {
                            ArrayList<Constant> array = new ArrayList<>();
                            for (int k = 0; k < nowDimension; k++) {
                                array.add(constantArray.get(j * nowDimension + k));
                            }
                            nowConstantArray.add(IRPort.getConstantArray(array));
                        }
                        constantArray = nowConstantArray;
                    }
                    ConstantArray initVal = IRPort.getConstantArray(constantArray);
                    globalVariable = IRPort.getGlobalVariable(varName, initVal, true);
                }

                symbolVal.get(symbolValIndex).put(varName, globalVariable);
                Module.getInstance().addGlobalVariable(globalVariable);
            } else {  // 局部变量
                Alloca alloca = IRPort.buildAlloca(arrayType, currentBasicBlock);
                symbolVal.get(symbolValIndex).put(ident.getWordValue(), alloca);

                currentDimensions = new ArrayList<>(dimensions);
                isArrayInit = true;
                visitConstInitVal(constInitValNode);
                isArrayInit = false;

                ArrayList<Value> constantArray = new ArrayList<>();

                for (Value value : currentArray) {
                    constantArray.add(value);
                }

                currentArrayIndex = 0;
                constArrayInit(arrayType, alloca, dimensions, constantArray, 1);
                currentArrayIndex = 0;
            }

        }
    }

    private void constArrayInit(Type arrayType, Value base, ArrayList<Integer> dimensions, ArrayList<Value> constantArray, int depth) {
        for (int i = 0; i < dimensions.get(depth - 1); i++) {
            Getelementptr getelementptr  = IRPort.buildGetelementptr(((ArrayType) arrayType), currentBasicBlock, base,
                    IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, i));
            if (depth == dimensions.size()) {
                IRPort.buildStore(currentBasicBlock, constantArray.get(currentArrayIndex++), getelementptr);
            } else {
                constArrayInit(((ArrayType) arrayType).getElementType(), getelementptr, dimensions, constantArray, depth + 1);
            }
        }
    }

    /*
        常量初值 ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
     */
    public void visitConstInitVal(ConstInitValNode constInitValNode) {
        ConstExpNode constExpNode = constInitValNode.getConstExpNode();
        ArrayList<ConstInitValNode> constInitValNodes = constInitValNode.getConstInitValNodes();

        if (constExpNode != null) {
            if (isGlobalInit || !isArrayInit) {
                isConstant = true;
                visitConstExp(constExpNode);
                isConstant = false;
            } else {
                visitConstExp(constExpNode);
            }
        } else {
            ArrayList<Value> array = new ArrayList<>();
            int arraySize = 1;
            for (int i = 1; i < currentDimensions.size(); i++) {
                arraySize  = arraySize * currentDimensions.get(i);
            }
            for (ConstInitValNode node : constInitValNodes) {
                if (node.getConstExpNode() != null) {
                    visitConstInitVal(node);
                    array.add(currentValue);
                } else {
                    currentDimensions = new ArrayList<Integer>() {{
                        addAll(Collections.singleton(currentDimensions.remove(0)));
                    }};
                    visitConstInitVal(node);
                    array.addAll(currentArray);
                }
            }
            currentArray = array;
        }
    }

    /*
        变量声明 VarDecl → 'int' VarDef { ',' VarDef } ';'
     */
    public void visitVarDecl(VarDeclNode varDeclNode) {
        ArrayList<VarDefNode> varDefNodes = varDeclNode.getVarDefNodes();

        for (VarDefNode varDefNode : varDefNodes) {
            visitVarDef(varDefNode);
        }
    }

    /*
        变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal | Ident '=' 'getint' '(' ')'
     */
    public void visitVarDef(VarDefNode varDefNode) {
        Word ident = varDefNode.getIdent();
        ArrayList<ConstExpNode> constExpNodes = varDefNode.getConstExpNodes();
        InitValNode initValNode = varDefNode.getInitValNode();
        boolean isGetint = varDefNode.isGetint();

        String varName = ident.getWordValue();
        if (constExpNodes.isEmpty()) {
            if (symbolValIndex == 0) {
                if (isGetint) {
//                    isGlobalInit = true;
//                    // 函数调用
//                    String funcName = "getint";
//                    Function calledFunction = Module.getInstance().getFunction("@" + funcName);
//                    ArrayList<Value> args = new ArrayList<>();
//                    currentValue = IRPort.buildCallWithReturn(currentBasicBlock, calledFunction, args);
//                    isGlobalInit = false;
//                    GlobalVariable globalVariable = IRPort.getGlobalVariable(varName, (Constant) currentValue, false);
//                    symbolVal.get(symbolValIndex).put(varName, globalVariable);
//                    Module.getInstance().addGlobalVariable(globalVariable);
                } else if (initValNode == null) {
                    GlobalVariable globalVariable = IRPort.getZeroGlobalVariable(varName, IRPort.getIntType(32));
                    symbolVal.get(symbolValIndex).put(varName, globalVariable);
                    Module.getInstance().addGlobalVariable(globalVariable);
                } else {
                    isGlobalInit = true;
                    visitInitVal(initValNode);
                    isGlobalInit = false;
                    GlobalVariable globalVariable = IRPort.getGlobalVariable(varName, (Constant) currentValue, false);
                    symbolVal.get(symbolValIndex).put(varName, globalVariable);
                    Module.getInstance().addGlobalVariable(globalVariable);
                }
            } else {
                Alloca alloca = IRPort.buildAlloca(IRPort.getIntType(32), currentBasicBlock);
                symbolVal.get(symbolValIndex).put(varName, alloca);
                if (isGetint) {
                    // 函数调用
                    String funcName = "getint";
                    Function calledFunction = Module.getInstance().getFunction("@" + funcName);
                    ArrayList<Value> args = new ArrayList<>();

                    currentValue = IRPort.buildCallWithReturn(currentBasicBlock, calledFunction, args);

                    IRPort.buildStore(currentBasicBlock, currentValue, alloca);
                } else if (initValNode != null) {
                    visitInitVal(initValNode);
                    IRPort.buildStore(currentBasicBlock, currentValue, alloca);
                }
            }
        } else {  // array
            ArrayList<Integer> dimensions = new ArrayList<>();

            for (ConstExpNode constExpNode : constExpNodes) {
                isConstant = true;
                visitConstExp(constExpNode);
                isConstant = false;
                dimensions.add(((ConstantInt) currentValue).getVal());
            }

            Type arrayType = new IntegerType(32);

            for (int i = dimensions.size() - 1; i >= 0; i--) {
                arrayType = IRPort.getArrayType(arrayType, dimensions.get(i));
            }

            if (symbolValIndex == 0) {  // 全局变量
                if (initValNode == null) {
                    GlobalVariable globalVariable = IRPort.getZeroGlobalVariable(varName, arrayType);
                    symbolVal.get(symbolValIndex).put(varName, globalVariable);
                    Module.getInstance().addGlobalVariable(globalVariable);
                } else {
                    currentDimensions = new ArrayList<>(dimensions);

                    if (constExpNodes.size() == 1) {
                        if (dimensions.get(0) != initValNode.getInitValNodes().size()) {
                            Pair error = new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.g);
                            System.err.println(error.getKey() + " " + error.getValue());
                            System.exit(0);
                        }
                    }

                    isGlobalInit = true;
                    visitInitVal(initValNode);
                    isGlobalInit = false;

                    ArrayList<Constant> constantArray = new ArrayList<>();

                    // 全零优化
                    boolean isZero = true;
                    for (Value value : currentArray) {
                        if (((ConstantInt) value).getVal() != 0) {
                            isZero = false;
                        }
                        constantArray.add((ConstantInt) value);
                    }
                    int arraySize = 1;
                    for (Integer dimension : dimensions) {
                        arraySize = arraySize * dimension;
                    }
                    GlobalVariable globalVariable;
                    if (isZero) {
                        globalVariable = IRPort.getZeroGlobalVariable(varName, arrayType);
                    } else {
                        // 根据所有的常量值和数组维度信息构建数组常量
                        for (int i = dimensions.size() - 1; i > 0; i--) {
                            int nowDimension = dimensions.get(i);
                            ArrayList<Constant> nowConstantArray = new ArrayList<>();
                            arraySize = arraySize / nowDimension;
                            for (int j = 0; j < arraySize; j++) {
                                ArrayList<Constant> array = new ArrayList<>();
                                for (int k = 0; k < nowDimension; k++) {
                                    array.add(constantArray.get(j * nowDimension + k));
                                }
                                nowConstantArray.add(IRPort.getConstantArray(array));
                            }
                            constantArray = nowConstantArray;
                        }
                        Constant initVal = IRPort.getConstantArray(constantArray);
                        globalVariable = IRPort.getGlobalVariable(varName, initVal, false);
                    }

                    symbolVal.get(symbolValIndex).put(varName, globalVariable);
                    Module.getInstance().addGlobalVariable(globalVariable);
                }
            } else {  // 局部变量
                Alloca alloca = IRPort.buildAlloca(arrayType, currentBasicBlock);
                symbolVal.get(symbolValIndex).put(ident.getWordValue(), alloca);

                if (initValNode != null) {
                    currentDimensions = new ArrayList<>(dimensions);
                    visitInitVal(initValNode);

                    if (constExpNodes.size() == 1) {
                        if (dimensions.get(0) != initValNode.getInitValNodes().size()) {
                            Pair error = new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.g);
                            System.err.println(error.getKey() + " " + error.getValue());
                            System.exit(0);
                        }
                    }

                    ArrayList<Value> valArray = new ArrayList<>();

                    for (Value value : currentArray) {
                        valArray.add(value);
                    }

                    currentArrayIndex = 0;
                    constArrayInit(arrayType, alloca, dimensions, valArray, 1);
                    currentArrayIndex = 0;
                }
            }
        }
    }

    /*
        变量初值 InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
     */
    public void visitInitVal(InitValNode initValNode) {
        ExpNode expNode = initValNode.getExpNode();
        ArrayList<InitValNode> initValNodes = initValNode.getInitValNodes();

        if (expNode != null) {
            if (isGlobalInit) {
                isConstant = true;
                visitExp(expNode);
                isConstant = false;
                currentValue = IRPort.getConstantInt(32, currentInt);
            } else {
                visitExp(expNode);
            }
        } else {
            if (initValNodes != null) {
                ArrayList<Value> array = new ArrayList<>();
                int arrayLength = currentDimensions.get(0);
                int arraySize = 1;
                for (int i = 1; i < currentDimensions.size(); i++) {
                    arraySize  = arraySize * currentDimensions.get(i);
                }
                for (InitValNode node : initValNodes) {
                    if (node.getExpNode() != null) {
                        if (isGlobalInit) {
                            isConstant = true;
                            visitInitVal(node);
                            isConstant = false;
                        } else {
                            visitInitVal(node);
                        }
                        array.add(currentValue);
                    } else {
                        currentDimensions = new ArrayList<Integer>() {{
                            addAll(Collections.singleton(currentDimensions.remove(0)));
                        }};
                        visitInitVal(node);
                        array.addAll(currentArray);
                    }
                }
                currentArray = array;
            }
        }
    }

    /*
        函数定义 FuncDef → ('void' | 'int') Ident '(' [FuncFParams] ')' Block
     */
    public void visitFuncDef(FuncDefNode funcDefNode) {
        FuncTypeNode funcTypeNode = funcDefNode.getFuncTypeNode();
        Word ident = funcDefNode.getIdent();
        FuncFParamsNode funcFParamsNode = funcDefNode.getFuncFParamsNode();
        BlockNode blockNode = funcDefNode.getBlockNode();

        String funcName = ident.getWordValue();

        ArrayList<Type> funcFParams = new ArrayList<>();
        if (funcFParamsNode != null) {
            visitFuncFParams(funcFParamsNode);
            for (Type type : currentTypeArray) {
                funcFParams.add(type);
            }
        }

        // Build function
        FunctionType functionType;
        if (funcTypeNode.getReserved().isInt()) {
            functionType = IRPort.getFunctionType(funcFParams, IRPort.getIntType(32));
        } else { // void
            functionType = IRPort.getFunctionType(funcFParams, IRPort.getVoidType());
        }
        Function function = IRPort.buildFunction(funcName, functionType);
        currentFunction = function;

        // Build basicblock
        BasicBlock basicBlock = IRPort.buildBasicBlock(function);
        currentBasicBlock = basicBlock;

        symbolValIndex++;
        symbolVal.add(new HashMap<>());
        isEnterSymbolVal = true;

        if (funcFParamsNode != null) {
            ArrayList<FuncFParamNode> funcFParamNodes = funcFParamsNode.getFuncFParamNodes();
            int funcFParamsCnt = 0;
            for (FuncFParamNode funcFParamNode : funcFParamNodes) {
                if (funcFParamNode.getConstExpNodes() == null) {
                    Alloca alloca = IRPort.buildAlloca(IRPort.getIntType(32), currentBasicBlock);
                    IRPort.buildStore(currentBasicBlock, currentFunction.getArguments().get(funcFParamsCnt), alloca);
                    symbolVal.get(symbolValIndex).put(funcFParamNode.getIdent().getWordValue(), alloca);
                } else {
                    Type arrayType = IRPort.getIntType(32);
                    for (int i = funcFParamNode.getConstExpNodes().size() - 1; i >= 0; i--) {
                        isConstant = true;
                        visitConstExp(funcFParamNode.getConstExpNodes().get(i));
                        isConstant = false;
                        arrayType = IRPort.getArrayType(arrayType, currentInt);
                    }
                    Alloca alloca = IRPort.buildAlloca(IRPort.getPointerType(arrayType), currentBasicBlock);
                    IRPort.buildStore(currentBasicBlock, currentFunction.getArguments().get(funcFParamsCnt), alloca);
                    symbolVal.get(symbolValIndex).put(funcFParamNode.getIdent().getWordValue(), alloca);
                }
                funcFParamsCnt++;
            }
        }

        visitBlock(blockNode);

        if (currentFunction.getReturnType() instanceof VoidType) {
            if (currentBasicBlock.getInstructions().size() == 0) {
                IRPort.buildRetNoReturn(currentBasicBlock);
            } else {
                if (!(currentBasicBlock.getInstructions().get(currentBasicBlock.getInstructions().size() - 1) instanceof Ret)) {
                    IRPort.buildRetNoReturn(currentBasicBlock);
                }
            }
        }
    }

    /*
        主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block
     */
    public void visitMainFuncDef(MainFuncDefNode mainFuncDefNode) {
        BlockNode blockNode = mainFuncDefNode.getBlockNode();
        String funcName = mainFuncDefNode.getIdent().getWordValue();

        // Build function
        FunctionType functionType = IRPort.getFunctionType(new ArrayList<Type>(), IRPort.getIntType(32));
        Function function = IRPort.buildFunction(funcName, functionType);
        currentFunction = function;

        // Build basicblock
        BasicBlock basicBlock = IRPort.buildBasicBlock(function);
        currentBasicBlock = basicBlock;

        symbolValIndex++;
        symbolVal.add(new HashMap<>());
        isEnterSymbolVal = true;

        visitBlock(blockNode);
    }

    /*
        函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
     */
    public void visitFuncFParams(FuncFParamsNode funcFParamsNode) {
        ArrayList<FuncFParamNode> funcFParamNodes = funcFParamsNode.getFuncFParamNodes();
        
        ArrayList<Type> funcFParamTypes = new ArrayList<>();
        for (FuncFParamNode funcFParamNode : funcFParamNodes) {
            visitFuncFParam(funcFParamNode);
            funcFParamTypes.add(currentType);
        }
        currentTypeArray = funcFParamTypes;
    }

    /*
        函数形参 FuncFParam → 'int' Ident ['[' ']' { '[' ConstExp ']' }]
     */
    public void visitFuncFParam(FuncFParamNode funcFParamNode) {
        Word ident = funcFParamNode.getIdent();
        ArrayList<ConstExpNode> constExpNodes = funcFParamNode.getConstExpNodes();

        if (constExpNodes == null) {
            currentType = IRPort.getIntType(32);
        } else {  // array
            Type type = new IntegerType(32);
            for (int i = constExpNodes.size() - 1; i >= 0; i--) {
                isConstant = true;
                visitConstExp(constExpNodes.get(i));
                isConstant = false;
                type = IRPort.getArrayType(type, currentInt);
            }
            currentType = IRPort.getPointerType(type);
        }
    }

    /*
        语句块 Block → '{' { BlockItem } '}'
     */
    public void visitBlock(BlockNode blockNode) {
        ArrayList<BlockItemNode> blockItemNodes = blockNode.getBlockItemNodes();

        if (isEnterSymbolVal) {
            isEnterSymbolVal = false;
        } else {
            symbolValIndex++;
            symbolVal.add(new HashMap<>());
        }

        for (BlockItemNode blockItemNode : blockItemNodes) {
            visitBlockItem(blockItemNode);
        }

        symbolVal.remove(symbolValIndex);
        symbolValIndex--;
    }

    /*
        语句块项 BlockItem → Decl | Stmt
     */
    public void visitBlockItem(BlockItemNode blockItemNode) {
        StmtNode stmtNode = blockItemNode.getStmtNode();
        DeclNode declNode = blockItemNode.getDeclNode();

        if (stmtNode != null) {
            visitStmt(stmtNode);
        } else {
            visitDecl(declNode);
        }
    }

    /*
        stmt : assignStmt | expStmt | blockStmt | branchStmt | loopStmt |
            breakStmt | continueStmt | returnStmt | getIntStmt | printfStmt;
     */
    public void visitStmt(StmtNode stmtNode) {
        if (stmtNode instanceof StmtAssignNode) {
            visitAssignStmt((StmtAssignNode) stmtNode);
        } else if (stmtNode instanceof StmtBlockNode) {
            visitBlockStmt((StmtBlockNode) stmtNode);
        } else if (stmtNode instanceof StmtBranchNode) {
            visitBranchStmt((StmtBranchNode) stmtNode);
        } else if (stmtNode instanceof StmtBreakNode) {
            visitBreakStmt((StmtBreakNode) stmtNode);
        } else if (stmtNode instanceof StmtContinueNode) {
            visitContinueStmt((StmtContinueNode) stmtNode);
        } else if (stmtNode instanceof StmtExprNode) {
            visitExpStmt((StmtExprNode) stmtNode);
        } else if (stmtNode instanceof StmtGetIntNode) {
            visitGetIntStmt((StmtGetIntNode) stmtNode);
        } else if (stmtNode instanceof StmtLoopNode) {
            visitLoopStmt((StmtLoopNode) stmtNode);
        } else if (stmtNode instanceof StmtPrintfNode) {
            visitPrintfStmt((StmtPrintfNode) stmtNode);
        } else if (stmtNode instanceof StmtReturnNode) {
            visitReturnStmt((StmtReturnNode) stmtNode);
        }
    }

    /*
        assignStmt → LVal '=' Exp ';'
     */
    public void visitAssignStmt(StmtAssignNode stmtAssignNode) {
        LValNode lValNode = stmtAssignNode.getlValNode();
        ExpNode expNode = stmtAssignNode.getExpNode();

        visitLVal(lValNode);
        Value leftOp = currentValue;
        visitExp(expNode);
        Value rightOp = currentValue;

        IRPort.buildStore(currentBasicBlock, rightOp, leftOp);
    }

    /*
        blockStmt → block
     */
    public void visitBlockStmt(StmtBlockNode stmtBlockNode) {
        BlockNode blockNode = stmtBlockNode.getBlockNode();

        visitBlock(blockNode);
    }

    /*
        expStmt → [Exp] ';'
     */
    public void visitExpStmt(StmtExprNode stmtExprNode) {
        ExpNode expNode = stmtExprNode.getExpNode();

        if (expNode != null) {
            visitExp(expNode);
        }
    }

    /*
        branchStmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
     */
    public void visitBranchStmt(StmtBranchNode stmtBranchNode) {
        CondNode condNode = stmtBranchNode.getCondNode();
        StmtNode stmtIfNode = stmtBranchNode.getStmtNodeIf();
        StmtNode stmtElseNode = stmtBranchNode.getStmtNodeElse();

        boolean ifHasReturn = false;
        boolean elseHasReturn = false;

        BasicBlock ifBasicBlock = IRPort.buildBasicBlock(currentFunction);
        BasicBlock elseBasicBlock = IRPort.buildBasicBlock(currentFunction);
        BasicBlock nextBasicBlock;
        if (stmtElseNode != null) {
            nextBasicBlock = IRPort.buildBasicBlock(currentFunction);
        } else {
            nextBasicBlock = elseBasicBlock;
        }

        currentIfBasicBlock = ifBasicBlock;
        currentElseBasicBlock = elseBasicBlock;

        visitCond(condNode);

        currentBasicBlock = ifBasicBlock;
        visitStmt(stmtIfNode);
        if (!currentBasicBlock.getInstructions().isEmpty()) {
            if (currentBasicBlock.getInstructions().get(currentBasicBlock.getInstructions().size() - 1) instanceof Ret) {
                ifHasReturn = true;
            } else if (currentBasicBlock.getInstructions().get(currentBasicBlock.getInstructions().size() - 1) instanceof Br) {
                ifHasReturn = true;
            } else {
                IRPort.buildBrNoCondition(currentBasicBlock, nextBasicBlock);
            }
        } else {
            IRPort.buildBrNoCondition(currentBasicBlock, nextBasicBlock);
        }

        if (stmtElseNode != null) {
            currentBasicBlock = elseBasicBlock;
            visitStmt(stmtElseNode);
            if (!currentBasicBlock.getInstructions().isEmpty()) {
                if (currentBasicBlock.getInstructions().get(currentBasicBlock.getInstructions().size() - 1) instanceof Ret) {
                    elseHasReturn = true;
                } else if (currentBasicBlock.getInstructions().get(currentBasicBlock.getInstructions().size() - 1) instanceof Br) {
                    elseHasReturn = true;
                } else {
                    IRPort.buildBrNoCondition(currentBasicBlock, nextBasicBlock);
                }
            } else {
                IRPort.buildBrNoCondition(currentBasicBlock, nextBasicBlock);
            }
        }

        if (ifHasReturn && elseHasReturn) {
            currentFunction.getBasicBlocks().remove(nextBasicBlock);  // 优化：剪枝
        } else {
            currentBasicBlock = nextBasicBlock;
        }
    }

    /*
        loopStmt → 'while' '(' Cond ')' Stmt
     */
    public void visitLoopStmt(StmtLoopNode stmtLoopNode) {
        CondNode condNode = stmtLoopNode.getCondNode();
        StmtNode stmtNode = stmtLoopNode.getStmtNode();

        BasicBlock breakBasicBlock = currentBreakBasicBlock;
        BasicBlock continueBasicBlock = currentContinueBasicBlock;

        BasicBlock condBasicBlock = IRPort.buildBasicBlock(currentFunction);
        BasicBlock loopBasicBlock = IRPort.buildBasicBlock(currentFunction);
        BasicBlock nextBasicBlock = IRPort.buildBasicBlock(currentFunction);

        IRPort.buildBrNoCondition(currentBasicBlock, condBasicBlock);

        currentIfBasicBlock = loopBasicBlock;
        currentElseBasicBlock = nextBasicBlock;

        currentBasicBlock = condBasicBlock;
        visitCond(condNode);

        currentBreakBasicBlock = nextBasicBlock;
        currentContinueBasicBlock = condBasicBlock;

        currentBasicBlock = loopBasicBlock;
        visitStmt(stmtNode);
        if (currentBasicBlock.getInstructions().isEmpty()) {
            IRPort.buildBrNoCondition(currentBasicBlock, condBasicBlock);
        } else {
            if (!(currentBasicBlock.getInstructions().get(currentBasicBlock.getInstructions().size() - 1) instanceof Br)) {
                IRPort.buildBrNoCondition(currentBasicBlock, condBasicBlock);
            }
        }

        currentBreakBasicBlock = breakBasicBlock;
        currentContinueBasicBlock = continueBasicBlock;

        currentBasicBlock = nextBasicBlock;
    }

    /*
        breakStmt → 'break' ';'
     */
    public void visitBreakStmt(StmtBreakNode stmtBreakNode) {
        BasicBlock breakBasicBlock = currentBreakBasicBlock;
        IRPort.buildBrNoCondition(currentBasicBlock, breakBasicBlock);
    }

    /*
        continueStmt → 'continue' ';'
     */
    public void visitContinueStmt(StmtContinueNode stmtContinueNode) {
        BasicBlock continueBasicBlock = currentContinueBasicBlock;
        IRPort.buildBrNoCondition(currentBasicBlock, continueBasicBlock);
    }

    /*
        returnStmt → 'return' [Exp] ';'
     */
    public void visitReturnStmt(StmtReturnNode stmtReturnNode) {
        ExpNode expNode = stmtReturnNode.getExpNode();

        if (expNode != null) {
            visitExp(expNode);
            currentValue = IRPort.buildRetWithReturn(currentBasicBlock, currentValue);
        } else {
            IRPort.buildRetNoReturn(currentBasicBlock);
        }
    }

    /*
        getIntStmt → LVal '=' 'getint''('')'';'
     */
    public void visitGetIntStmt(StmtGetIntNode stmtGetIntNode) {
        LValNode lValNode = stmtGetIntNode.getlValNode();

        visitLVal(lValNode);
        Value leftOp = currentValue;

        // 函数调用
        String funcName = "getint";
        Function calledFunction = Module.getInstance().getFunction("@" + funcName);
        ArrayList<Value> args = new ArrayList<>();

        currentValue = IRPort.buildCallWithReturn(currentBasicBlock, calledFunction, args);

        Value rightOp = currentValue;

        IRPort.buildStore(currentBasicBlock, rightOp, leftOp);
    }

    /*
        printfStmt → 'printf''('FormatString{','Exp}')'';'
     */
    public void visitPrintfStmt(StmtPrintfNode stmtPrintfNode) {
        Word formatString = stmtPrintfNode.getFormatString();
        ArrayList<ExpNode> expNodes = stmtPrintfNode.getExpNodes();

        char[] strArray = formatString.getWordValue().substring(1, formatString.getWordValue().length() - 1).toCharArray();
        int expCnt = 0;

        ArrayList<Value> expValues = new ArrayList<>();

        for (int i = 0; i < strArray.length; i++) {
            if (i != strArray.length - 1) {
                if (strArray[i] == '%' && strArray[i + 1] == 'd') {
                    visitExp(expNodes.get(expCnt));
                    expValues.add(currentValue);
                    expCnt++;
                    i++;
                }
            }
        }

        expCnt = 0;

        for (int i = 0; i < strArray.length; i++) {
            String str;
            boolean isPutInt = false;
            if (i == strArray.length - 1) {
                str = String.valueOf(strArray[i]);
            } else {
                StringBuilder sb = new StringBuilder();
                while (i < strArray.length) {
                    if (i == strArray.length - 1) {
                        sb.append(strArray[i]);
                        break;
                    } else {
                        if (strArray[i] == '%' && strArray[i + 1] == 'd') {
                            i++;
                            isPutInt = true;
                            break;
                        } else if (strArray[i] == '\\' && strArray[i + 1] == 'n') {
                            i += 2;
                            sb.append("\n");
                            continue;
                        }
                        sb.append(strArray[i]);
                    }
                    i++;
                }
                str = sb.toString();
            }
            // 函数调用
            String funcName = "putstr";
            Function calledFunction = Module.getInstance().getFunction("@" + funcName);
            ArrayList<Value> args = new ArrayList<>();

            GlobalVariable globalVariable = IRPort.getStringGlobalVariable(str);

            symbolVal.get(symbolValIndex).put(globalVariable.getName(), globalVariable);
            Module.getInstance().addGlobalVariable(globalVariable);

            Value value = IRPort.buildGetelementptr((PointerType) globalVariable.getType(), currentBasicBlock, globalVariable, IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, 0));

            args.add(value);

            currentValue = IRPort.buildCallNoReturn(currentBasicBlock, calledFunction, args);
            if (isPutInt) {
                // 函数调用
                funcName = "putint";
                calledFunction = Module.getInstance().getFunction("@" + funcName);
                args = new ArrayList<>();

                args.add(expValues.get(expCnt));
                expCnt++;

                currentValue = IRPort.buildCallNoReturn(currentBasicBlock, calledFunction, args);
            }
        }

    }

    /*
        表达式 Exp → AddExp
     */
    public void visitExp(ExpNode expNode) {
        AddExpNode addExpNode = expNode.getAddExpNode();

        visitAddExp(addExpNode);
    }

    /*
        条件表达式 Cond → LOrExp
     */
    public void visitCond(CondNode condNode) {
        LOrExpNode lOrExpNode = condNode.getlOrExpNode();

        visitLOrExp(lOrExpNode, true);
    }

    /*
        左值表达式 LVal → Ident {'[' Exp ']'}
     */
    public void visitLVal(LValNode lValNode) {
        Word ident = lValNode.getIdent();
        ArrayList<ExpNode> expNodes = lValNode.getExpNodes();

        String valueName = ident.getWordValue();
        Value value = null;
        for (int i = symbolValIndex; i >= 0; i--) {
            if (symbolVal.get(i).containsKey(valueName)) {
                value = symbolVal.get(i).get(valueName);
                break;
            }
        }
        if (value.getType() instanceof IntegerType) {
            currentValue = value;
        } else if (value.getType() instanceof PointerType) {
            Type type = ((PointerType) value.getType()).getPointToType();
            if (type instanceof IntegerType) {
                currentValue = value;
            } else if (type instanceof ArrayType) {  // arrayType
                ArrayType arrayType = ((ArrayType) type);

                if (isGlobalInit || isConstant) {
                    if (!expNodes.isEmpty()) {
                        value = ((GlobalVariable) value).getVal();
                        for (int i = 0; i < expNodes.size(); i++) {
                            visitExp(expNodes.get(i));

                            value = (((ConstantArray) value).getUsedValue(currentInt));
                        }
                    }
                } else {
                    if (expNodes.isEmpty()) {
                        value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, 0));
                    } else {
                        for (int i = 0; i < expNodes.size(); i++) {
                            visitExp(expNodes.get(i));
                            value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), currentValue);

                            if (arrayType.getElementType() instanceof ArrayType) {
                                arrayType = ((ArrayType) arrayType.getElementType());

                                if (i == expNodes.size() - 1) {
                                    value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, 0));
                                }
                            }
                        }
                    }
                }

                currentValue = value;
            } else {  // pointType
                PointerType pointerType = ((PointerType) type);
                Load load = IRPort.buildLoad(pointerType, currentBasicBlock, value);
                value = load;
                ArrayType arrayType = null;

                if (!expNodes.isEmpty()) {
                    visitExp(expNodes.get(0));
                    value = IRPort.buildGetelementptr(pointerType, currentBasicBlock, load, currentValue);
                    if (pointerType.getPointToType() instanceof ArrayType) {
                        arrayType = (ArrayType) (pointerType.getPointToType());

                        if (expNodes.size() == 1) {
                            value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, 0));
                        }
                    }

                    for (int i = 1; i < expNodes.size(); i++) {
                        visitExp(expNodes.get(i));
                        value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), currentValue);
                        if (arrayType.getElementType() instanceof ArrayType) {
                            arrayType = ((ArrayType) arrayType.getElementType());

                            if (i == expNodes.size() - 1) {
                                value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, 0));
                            }
                        }
                    }
                }

                currentValue = value;
            }
        } else {  // arrayType
            ArrayType arrayType = ((ArrayType) value.getType());

            for (int i = 0; i < expNodes.size(); i++) {
                visitExp(expNodes.get(i));
                value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), currentValue);

                if (arrayType.getElementType() instanceof ArrayType) {
                    arrayType = ((ArrayType) arrayType.getElementType());

                    if (i == expNodes.size() - 1) {
                        value = IRPort.buildGetelementptr(arrayType, currentBasicBlock, value, IRPort.getConstantInt(32, 0), IRPort.getConstantInt(32, 0));
                    }
                }
            }

            currentValue = value;
        }
    }

    /*
        基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
     */
    public void visitPrimaryExp(PrimaryExpNode primaryExpNode) {
        ExpNode expNode = primaryExpNode.getExpNode();
        LValNode lValNode = primaryExpNode.getlValNode();
        NumberNode numberNode = primaryExpNode.getNumberNode();

        if (expNode != null) {
            visitExp(expNode);
        } else if (lValNode != null) {
            if (isConstant) {
                visitLVal(lValNode);
                currentInt = ((ConstantInt) currentValue).getVal();
            } else {
                boolean nowIsCallingFunc = isCallingFunc;
                if (isCallingFunc) {
                    isCallingFunc = false;
                }
                visitLVal(lValNode);
                if (!nowIsCallingFunc) {
                    if (!(currentValue.getType() instanceof IntegerType)) {
                        if (((PointerType) currentValue.getType()).getPointToType() instanceof IntegerType) {
                            currentValue = IRPort.buildLoad(IRPort.getIntType(32), currentBasicBlock, currentValue);
                        }
                    }
                }
            }
        } else {
            visitNumber(numberNode);
        }
    }

    /*
        数值 Number → IntConst
     */
    public void visitNumber(NumberNode numberNode) {
        currentInt = new Integer(numberNode.getIntConst().getWordValue());
        if (!isConstant) {
            currentValue = IRPort.getConstantInt(32, currentInt);
        }
    }

    /*
        一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | ('+' | '−' | '!') UnaryExp
     */
    public void visitUnaryExp(UnaryExpNode unaryExpNode) {
        PrimaryExpNode primaryExpNode = unaryExpNode.getPrimaryExpNode();
        Word ident = unaryExpNode.getIdent();
        FuncRParamsNode funcRParamsNode = unaryExpNode.getFuncRParamsNode();
        UnaryOpNode unaryOpNode = unaryExpNode.getUnaryOpNode();
        UnaryExpNode unaryExpNode1 = unaryExpNode.getUnaryExpNode();

        if (isConstant) {  // 没有函数调用
            if (primaryExpNode != null) {
                visitPrimaryExp(primaryExpNode);
            } else {
                visitUnaryExp(unaryExpNode1);
                if (unaryOpNode.getSeparator().isMinu()) {
                    currentInt = -1 * currentInt;
                } else if (unaryOpNode.getSeparator().isNot()) {
                    if (currentInt == 0) {
                        currentInt = 1;
                    } else {
                        currentInt = 0;
                    }
                }
            }
        } else {
            if (primaryExpNode != null) {
                visitPrimaryExp(primaryExpNode);
            } else if (ident != null) {
                // 函数调用
                String funcName = ident.getWordValue();
                Function calledFunction = Module.getInstance().getFunction("@" + funcName);
                ArrayList<Value> args = new ArrayList<>();

                ArrayList<Type> argcTypes = ((FunctionType) calledFunction.getType()).getArgs();
                int argcCnt = 0;

                if (funcRParamsNode != null) {
                    for (ExpNode expNode : funcRParamsNode.getExpNodes()) {
                        if (!(argcTypes.get(argcCnt) instanceof IntegerType)) {
                            isCallingFunc = true;
                        }
                        visitExp(expNode);
                        isCallingFunc = false;

                        args.add(currentValue);
                        argcCnt++;
                    }
                }

                if (calledFunction.getReturnType() instanceof VoidType) {
                    currentValue = IRPort.buildCallNoReturn(currentBasicBlock, calledFunction, args);
                } else {
                    currentValue = IRPort.buildCallWithReturn(currentBasicBlock, calledFunction, args);
                }
            } else {
                visitUnaryExp(unaryExpNode1);
                if (unaryOpNode.getSeparator().isMinu()) {
                    currentValue = IRPort.buildSub(currentBasicBlock, IRPort.getConstantInt(32, 0), currentValue);
                } else if (unaryOpNode.getSeparator().isNot()) {
                    currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.EQ, IRPort.getConstantInt(32, 0), currentValue);
                }
            }
        }
    }

    /*
        乘除模表达式 MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
     */
    public void visitMulExp(MulExpNode mulExpNode) {
        MulExpNode mulExpNode1 = mulExpNode.getMulExpNode();
        Word separator = mulExpNode.getSepatator();
        UnaryExpNode unaryExpNode = mulExpNode.getUnaryExpNode();

        if (isConstant) {
            if (mulExpNode1 != null) {
                visitMulExp(mulExpNode1);
                int leftOp = currentInt;

                visitUnaryExp(unaryExpNode);
                if (separator.isMult()) {
                    currentInt = leftOp * currentInt;
                } else if (separator.isDiv()) {
                    currentInt = leftOp / currentInt;
                } else {
                    currentInt = leftOp % currentInt;
                }
            } else {
                visitUnaryExp(unaryExpNode);
            }
        } else {
            if (mulExpNode1 != null) {
                visitMulExp(mulExpNode1);
                Value leftOp = currentValue;

                visitUnaryExp(unaryExpNode);
                if (separator.isMult()) {
                    currentValue = IRPort.buildMul(currentBasicBlock, leftOp, currentValue);
                } else if (separator.isDiv()) {
                    currentValue = IRPort.buildSdiv(currentBasicBlock, leftOp, currentValue);
                } else if (separator.isMod()){
                    currentValue = IRPort.buildSrem(currentBasicBlock, leftOp, currentValue);
                } else {
                    currentValue = IRPort.buildAnd(currentBasicBlock, leftOp, currentValue);
                }
            } else {
                visitUnaryExp(unaryExpNode);
            }
        }
    }

    /*
        加减表达式 AddExp → MulExp | AddExp ('+' | '−') MulExp
     */
    public void visitAddExp(AddExpNode addExpNode) {
        AddExpNode addExpNode1 = addExpNode.getAddExpNode();
        Word separator = addExpNode.getSeparator();
        MulExpNode mulExpNode = addExpNode.getMulExpNode();

        if (isConstant) {
            if (addExpNode1 != null) {
                visitAddExp(addExpNode1);
                int leftOp = currentInt;

                visitMulExp(mulExpNode);
                if (separator.isPlus()) {
                    currentInt = leftOp + currentInt;
                } else {
                    currentInt = leftOp - currentInt;
                }
            } else {
                visitMulExp(mulExpNode);
            }
        } else {
            if (addExpNode1 != null) {
                visitAddExp(addExpNode1);
                Value leftOp = currentValue;

                visitMulExp(mulExpNode);
                if (separator.isPlus()) {
                    currentValue = IRPort.buildAdd(currentBasicBlock, leftOp, currentValue);
                } else {
                    currentValue = IRPort.buildSub(currentBasicBlock, leftOp, currentValue);
                }
            } else {
                visitMulExp(mulExpNode);
            }
        }
    }

    /*
        关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
     */
    public void visitRelExp(RelExpNode relExpNode, boolean first) {
        RelExpNode relExpNode1 = relExpNode.getRelExpNode();
        Word separator = relExpNode.getSeparator();
        AddExpNode addExpNode = relExpNode.getAddExpNode();

        if (relExpNode1 != null) {
            visitRelExp(relExpNode1, false);
            Value leftOp = currentValue;

            visitAddExp(addExpNode);
            if (separator.isLss()) {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.SLT, leftOp, currentValue);
            } else if (separator.isGre()) {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.SGT, leftOp, currentValue);
            } else if (separator.isLeq()) {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.SLE, leftOp, currentValue);
            } else {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.SGE, leftOp, currentValue);
            }
        } else {
            if (!first) {
                isSingleJudge = false;
            }
            visitAddExp(addExpNode);
        }

    }

    /*
        相等性表达式 EqExp → RelExp | EqExp ('==' | '!=') RelExp
     */
    public void visitEqExp(EqExpNode eqExpNode, boolean first) {
        EqExpNode eqExpNode1 = eqExpNode.getEqExpNode();
        Word separator = eqExpNode.getSeparator();
        RelExpNode relExpNode = eqExpNode.getRelExpNode();

        if (eqExpNode1 != null) {
            visitEqExp(eqExpNode1, false);
            Value leftOp = currentValue;

            visitRelExp(relExpNode, true);
            if (separator.isEql()) {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.EQ, leftOp, currentValue);
            } else {  // NE
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.NE, leftOp, currentValue);
            }
        } else {
            if (!first) {
                isSingleJudge = false;
            }
            visitRelExp(relExpNode, true);
        }
    }

    /*
        逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp
     */
    public void visitLAndExp(LAndExpNode lAndExpNode, boolean first) {
        LAndExpNode lAndExpNode1 = lAndExpNode.getlAndExpNode();
        EqExpNode eqExpNode = lAndExpNode.getEqExpNode();

        BasicBlock ifBasicBlock = currentIfBasicBlock;
        BasicBlock elseBasicBlock = currentElseBasicBlock;

        if (lAndExpNode1 != null) {
            visitLAndExp(lAndExpNode1, false);

            BasicBlock nextBasicBlock = IRPort.buildBasicBlock(currentFunction);
            isSingleJudge = true;
            visitEqExp(eqExpNode, true);
            if (isSingleJudge) {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.NE, currentValue, IRPort.getConstantInt(32, 0));
                isSingleJudge = false;
            }
            IRPort.buildBrWithCondition(currentBasicBlock, currentValue, nextBasicBlock, elseBasicBlock);
            currentBasicBlock = nextBasicBlock;

            if (first) {
                IRPort.buildBrNoCondition(currentBasicBlock, ifBasicBlock);
            }
        } else {
            if (first) {
                isSingleJudge = true;
                visitEqExp(eqExpNode, true);
                if (isSingleJudge) {
                    currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.NE, currentValue, IRPort.getConstantInt(32, 0));
                    isSingleJudge = false;
                }
                IRPort.buildBrWithCondition(currentBasicBlock, currentValue, ifBasicBlock, elseBasicBlock);
                currentBasicBlock = ifBasicBlock;
            } else {
                BasicBlock nextBasicBlock = IRPort.buildBasicBlock(currentFunction);
                isSingleJudge = true;
                visitEqExp(eqExpNode, true);
                if (isSingleJudge) {
                    currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.NE, currentValue, IRPort.getConstantInt(32, 0));
                    isSingleJudge = false;
                }
                IRPort.buildBrWithCondition(currentBasicBlock, currentValue, nextBasicBlock, elseBasicBlock);
                currentBasicBlock = nextBasicBlock;
            }
        }

        currentIfBasicBlock = ifBasicBlock;
        currentElseBasicBlock = elseBasicBlock;
    }

    /*
        逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp
     */
    public void visitLOrExp(LOrExpNode lOrExpNode, boolean first) {
        LOrExpNode lOrExpNode1 = lOrExpNode.getlOrExpNode();
        LAndExpNode lAndExpNode = lOrExpNode.getlAndExpNode();

        BasicBlock ifBasicBlock = currentIfBasicBlock;
        BasicBlock elseBasicBlock = currentElseBasicBlock;

        if (lOrExpNode1 != null) {
            visitLOrExp(lOrExpNode1, false);

            if (!first) {
                BasicBlock nextBasicBlock = IRPort.buildBasicBlock(currentFunction);
                currentElseBasicBlock = nextBasicBlock;
                visitLAndExp(lAndExpNode, true);
                currentBasicBlock = nextBasicBlock;
            } else {
                visitLAndExp(lAndExpNode, true);
            }
        } else {
            if (!first) {
                BasicBlock nextBasicBlock = IRPort.buildBasicBlock(currentFunction);
                currentElseBasicBlock = nextBasicBlock;
;
                visitLAndExp(lAndExpNode, true);
                currentBasicBlock = nextBasicBlock;
            } else {
                visitLAndExp(lAndExpNode, true);
            }
        }

        currentIfBasicBlock = ifBasicBlock;
        currentElseBasicBlock = elseBasicBlock;
    }

    /*
        常量表达式 ConstExp → AddExp
     */
    public void visitConstExp(ConstExpNode constExpNode) {
        AddExpNode addExpNode = constExpNode.getAddExpNode();

        visitAddExp(addExpNode);

        if (isConstant) {
            currentValue = IRPort.getConstantInt(32, currentInt);
        }
    }

}
