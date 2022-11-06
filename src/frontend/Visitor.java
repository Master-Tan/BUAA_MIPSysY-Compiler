package frontend;

import frontend.grammaticalAnalysis.grammatical.*;
import frontend.grammaticalAnalysis.grammatical.Stmt.*;
import frontend.lexicalAnalysis.lexical.Word;
import midend.ir.IRPort;
import midend.ir.types.*;
import midend.ir.values.*;
import midend.ir.values.constant.*;
import midend.ir.values.instructions.binary.IcmpType;
import midend.ir.values.instructions.memory.Alloca;

import java.util.ArrayList;
import java.util.HashMap;

public class Visitor {

    private Function currentFunction;
    private BasicBlock currentBasicBlock;

    private Value currentValue;
    private int currentInt;
    private Type currentType;
    private ArrayList<Value> currentArray;
    private ArrayList<Type> currentTypeArray;

    private boolean isConstant = false;
    private boolean isGlobalInit = false;
    private boolean isCallingFunc = false;

    private ArrayList<HashMap<String, Value>> symbolVal = new ArrayList<HashMap<String, Value>>(){{
        add(new HashMap<>());
    }};
    private int symbolValIndex = 0;
    private ArrayList<HashMap<String, Integer>> regRank = new ArrayList<HashMap<String, Integer>>(){{
        add(new HashMap<>());
    }};
    private boolean inEnterSymbolVal = false;

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

        String defName = ident.getWordValue();
        if (constExpNodes.isEmpty()) {
            visitConstInitVal(constInitValNode);
            symbolVal.get(symbolValIndex).put(defName, currentValue);
        } else {
            // TODO
        }
    }

    /*
        常量初值 ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
     */
    public void visitConstInitVal(ConstInitValNode constInitValNode) {
        ConstExpNode constExpNode = constInitValNode.getConstExpNode();
        ArrayList<ConstInitValNode> constInitValNodes = constInitValNode.getConstInitValNodes();

        if (constExpNode != null) {
            isConstant = true;
            visitConstExp(constExpNode);
            isConstant = false;
        } else if (constInitValNodes != null) {
            // TODO
        } else {
            // TODO
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
        变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
     */
    public void visitVarDef(VarDefNode varDefNode) {
        Word ident = varDefNode.getIdent();
        ArrayList<ConstExpNode> constExpNodes = varDefNode.getConstExpNodes();
        InitValNode initValNode = varDefNode.getInitValNode();

        String varName = ident.getWordValue();
        if (constExpNodes.isEmpty()) {
            if (symbolValIndex == 0) {
                if (initValNode == null) {
                    GlobalVariable globalVariable = IRPort.getGlobalVariable(varName, IRPort.getIntType(32));
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
                if (initValNode != null) {
                    visitInitVal(initValNode);
                    IRPort.buildStore(currentBasicBlock, currentValue, alloca);
                }
            }
        } else {
            // TODO
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
                // TODO
            } else {
                // TODO
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
        BasicBlock basicBlock = IRPort.buildBasicBlock(funcName, function);
        currentBasicBlock = basicBlock;

        symbolValIndex++;
        symbolVal.add(new HashMap<>());
        inEnterSymbolVal = true;

        if (funcFParamsNode != null) {
            ArrayList<FuncFParamNode> funcFParamNodes = funcFParamsNode.getFuncFParamNodes();
            int funcFParamsCnt = 0;
            for (FuncFParamNode funcFParamNode : funcFParamNodes) {
                if (funcFParamNode.getConstExpNodes() == null) {
                    Alloca alloca = IRPort.buildAlloca(IRPort.getIntType(32), currentBasicBlock);
                    IRPort.buildStore(currentBasicBlock, currentFunction.getArguments().get(funcFParamsCnt), alloca);
                    symbolVal.get(symbolValIndex).put(funcFParamNode.getIdent().getWordValue(), alloca);
                } else {
                    // TODO
                }
                funcFParamsCnt++;
            }
        }

        visitBlock(blockNode);

        if (currentFunction.getReturnType() instanceof VoidType) {
            IRPort.buildRetNoReturn(currentBasicBlock);
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
        BasicBlock basicBlock = IRPort.buildBasicBlock(funcName, function);
        currentBasicBlock = basicBlock;

        symbolValIndex++;
        symbolVal.add(new HashMap<>());
        inEnterSymbolVal = true;

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
        } else {
            // TODO
        }
    }

    /*
        语句块 Block → '{' { BlockItem } '}'
     */
    public void visitBlock(BlockNode blockNode) {
        ArrayList<BlockItemNode> blockItemNodes = blockNode.getBlockItemNodes();

        if (inEnterSymbolVal) {
            inEnterSymbolVal = false;
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
        // TODO
    }

    /*
        loopStmt → 'while' '(' Cond ')' Stmt
     */
    public void visitLoopStmt(StmtLoopNode stmtLoopNode) {
        // TODO
    }

    /*
        breakStmt → 'break' ';'
     */
    public void visitBreakStmt(StmtBreakNode stmtBreakNode) {
        // TODO
    }

    /*
        continueStmt → 'continue' ';'
     */
    public void visitContinueStmt(StmtContinueNode stmtContinueNode) {
        // TODO
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

        for (int i = 0; i < strArray.length; i++) {
            boolean isPutChar = false;
            boolean isChangeLine = false;
            if (i == strArray.length - 1) {
                isPutChar = true;
            } else {
                if (strArray[i] == '%' && strArray[i + 1] == 'd') {
                    i++;
                } else if (strArray[i] == '\\' && strArray[i + 1] == 'n') {
                    isPutChar = true;
                    isChangeLine = true;
                    i++;
                } else {
                    isPutChar = true;
                }
            }
            if (isPutChar) {
                // 函数调用
                String funcName = "putch";
                Function calledFunction = Module.getInstance().getFunction("@" + funcName);
                ArrayList<Value> args = new ArrayList<>();

                if (isChangeLine) {
                    args.add(IRPort.getConstantInt(32, '\n'));
                } else {
                    args.add(IRPort.getConstantInt(32, strArray[i]));
                }

                currentValue = IRPort.buildCallNoReturn(currentBasicBlock, calledFunction, args);
            } else {
                // 函数调用
                String funcName = "putint";
                Function calledFunction = Module.getInstance().getFunction("@" + funcName);
                ArrayList<Value> args = new ArrayList<>();

                visitExp(expNodes.get(expCnt));
                args.add(currentValue);
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

        // TODO
        visitLOrExp(lOrExpNode);
    }

    /*
        左值表达式 LVal → Ident {'[' Exp ']'}
     */
    public void visitLVal(LValNode lValNode) {
        Word ident = lValNode.getIdent();
        ArrayList<ExpNode> expNodes = lValNode.getExpNodes();

        if (expNodes.isEmpty()) {
            String valueName = ident.getWordValue();
            Value value = null;
            for (int i = symbolValIndex; i >= 0; i--) {
                if (symbolVal.get(i).containsKey(valueName)) {
                    value = symbolVal.get(i).get(valueName);
                    break;
                }
            }
            currentValue = value;
        } else {
            // TODO
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
                visitLVal(lValNode);
                if (!isCallingFunc) {
                    if (!(currentValue.getType() instanceof IntegerType)) {
                        if (((PointerType) currentValue.getType()).getPointToType() instanceof IntegerType) {
                            currentValue = IRPort.buildLoad(IRPort.getIntType(32), currentBasicBlock, currentValue);
                        }
                    }
                } else {
                    isCallingFunc = false;
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
                } else {
                    currentValue = IRPort.buildSrem(currentBasicBlock, leftOp, currentValue); // TODO 可能含有错误
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
    public void visitRelExp(RelExpNode relExpNode) {
        RelExpNode relExpNode1 = relExpNode.getRelExpNode();
        Word separator = relExpNode.getSeparator();
        AddExpNode addExpNode = relExpNode.getAddExpNode();

        if (relExpNode1 != null) {
            visitRelExp(relExpNode1);
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
            visitAddExp(addExpNode);
        }

    }

    /*
        相等性表达式 EqExp → RelExp | EqExp ('==' | '!=') RelExp
     */
    public void visitEqExp(EqExpNode eqExpNode) {
        EqExpNode eqExpNode1 = eqExpNode.getEqExpNode();
        Word separator = eqExpNode.getSeparator();
        RelExpNode relExpNode = eqExpNode.getRelExpNode();

        if (eqExpNode1 != null) {
            visitEqExp(eqExpNode1);
            Value leftOp = currentValue;

            visitRelExp(relExpNode);
            if (separator.isEql()) {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.EQ, leftOp, currentValue);
            } else {
                currentValue = IRPort.buildIcmp(currentBasicBlock, IcmpType.NE, leftOp, currentValue);
            }
        } else {
            visitRelExp(relExpNode);
        }
    }

    /*
        逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp
     */
    public void visitLAndExp(LAndExpNode lAndExpNode) {
        LAndExpNode lAndExpNode1 = lAndExpNode.getlAndExpNode();
        EqExpNode eqExpNode = lAndExpNode.getEqExpNode();

        if (lAndExpNode1 != null) {
            visitLAndExp(lAndExpNode1);
            Value leftOp = currentValue;

            visitEqExp(eqExpNode);
            currentValue = IRPort.buildAnd(currentBasicBlock, leftOp, currentValue);
        } else {
            visitEqExp(eqExpNode);
        }
    }

    /*
        逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp
     */
    public void visitLOrExp(LOrExpNode lOrExpNode) {
        LOrExpNode lOrExpNode1 = lOrExpNode.getlOrExpNode();
        LAndExpNode lAndExpNode = lOrExpNode.getlAndExpNode();

        if (lOrExpNode1 != null) {
            visitLOrExp(lOrExpNode1);
            Value leftOp = currentValue;

            visitLAndExp(lAndExpNode);
            currentValue = IRPort.buildOr(currentBasicBlock, leftOp, currentValue);
        } else {
            visitLAndExp(lAndExpNode);
        }
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
