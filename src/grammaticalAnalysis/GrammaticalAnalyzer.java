package grammaticalAnalysis;

import exceptions.SysYException;
import grammaticalAnalysis.grammatical.*;
import grammaticalAnalysis.grammatical.AddExpNode;
import grammaticalAnalysis.grammatical.ExpNode;
import grammaticalAnalysis.grammatical.LValNode;
import grammaticalAnalysis.grammatical.MulExpNode;
import grammaticalAnalysis.grammatical.PrimaryExpNode;
import grammaticalAnalysis.grammatical.UnaryExpNode;
import grammaticalAnalysis.grammatical.Stmt.*;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.lexical.Word;
import myclasses.Pair;

import java.util.ArrayList;

public class GrammaticalAnalyzer {

    public static final boolean printGrammaticalAnalyzerData = false;  // 是否输出语法分析结果

    private ArrayList<Word> words;
    private int index;
    private int wordsLength;
    private int line;

    private SymbolTable currentSymbolTable;

    public GrammaticalAnalyzer(ArrayList<Word> words) {

        this.words = words;
        this.index = 0;
        this.wordsLength = this.words.size();
        this.line = 1;

        SymbolTable initSymbolTable = new SymbolTable();
        currentSymbolTable = initSymbolTable;

        CompUnitNode compUnitNode = compUnit();  // 语法树根结点

    }

    private CompUnitNode compUnit() {
        ArrayList<DeclNode> declNodes = new ArrayList<>();
        while (isDecl()) {
            DeclNode declNode = decl();
            declNodes.add(declNode);
        }
        ArrayList<FuncDefNode> funcDefNodes = new ArrayList<>();
        while (isFuncDef()) {
            FuncDefNode funcDefNode = funcDef();
            funcDefNodes.add(funcDefNode);
        }

        MainFuncDefNode mainFuncDefNode = mainFuncDef();

        CompUnitNode compUnitNode = new CompUnitNode(declNodes, funcDefNodes, mainFuncDefNode, line);

        printGrammaticalData("CompUnit");

        return compUnitNode;
    }

    private FuncDefNode funcDef() {

        FuncDefNode funcDefNode = null;

        FuncTypeNode funcTypeNode = funcType();

        Word ident = words.get(index);
        printWord(ident);

        Word separator = words.get(index);
        printWord(separator);

        currentSymbolTable.toChild();

        if (words.get(index).isRParent()) {
            printWord(words.get(index));

            BlockNode blockNode = block();

            funcDefNode = new FuncDefNode(funcTypeNode, ident, blockNode, line);
        } else {
            if (isFuncFParams()) {
                FuncFParamsNode funcFParamsNode = funcFParams();

                printWord(words.get(index));

                BlockNode blockNode = block();

                funcDefNode = new FuncDefNode(funcTypeNode, ident, funcFParamsNode, blockNode, line);
            } else {
                Node.errors.add(new Pair<>(separator.getLineNumber(), SysYException.ExceptionCode.j));

                BlockNode blockNode = block();

                funcDefNode = new FuncDefNode(funcTypeNode, ident, blockNode, line);
            }
        }

        printGrammaticalData("FuncDef");

        if (!funcDefNode.checkErrorB(currentSymbolTable)) {
            currentSymbolTable.addVar(funcDefNode.getIdent(), funcDefNode);
        }

        funcDefNode.checkErrorF(currentSymbolTable);
        funcDefNode.checkErrorG(currentSymbolTable);

        return funcDefNode;
    }

    private FuncTypeNode funcType() {

        Word reserved = words.get(index);
        printWord(reserved);

        FuncTypeNode funcTypeNode = new FuncTypeNode(reserved, line);

        printGrammaticalData("FuncType");

        return funcTypeNode;
    }

    private FuncFParamsNode funcFParams() {

        ArrayList<FuncFParamNode> funcFParamNodes = new ArrayList<>();
        FuncFParamNode funcFParamNode = funcFParam();
        funcFParamNodes.add(funcFParamNode);

        while (words.get(index).isComma()) {
            printWord(words.get(index));

            funcFParamNode = funcFParam();
            funcFParamNodes.add(funcFParamNode);

        }

        FuncFParamsNode funcFParamsNode = new FuncFParamsNode(funcFParamNodes, line);

        printGrammaticalData("FuncFParams");

        return funcFParamsNode;
    }

    private FuncFParamNode funcFParam() {

        FuncFParamNode funcFParamNode = null;

        printWord(words.get(index));

        Word ident = words.get(index);
        printWord(ident);

        if (words.get(index).isLbrack()) {

            printWord(words.get(index));

            if (words.get(index).isRbrack()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.k));
            }

            ArrayList<ConstExpNode> constExpNodes = new ArrayList<>();
            while (words.get(index).isLbrack()) {
                printWord(words.get(index));

                ConstExpNode constExpNode = constExp();
                constExpNodes.add(constExpNode);

                if (words.get(index).isRbrack()) {
                    printWord(words.get(index));
                } else {
                    Node.errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.k));
                }
            }

            funcFParamNode = new FuncFParamNode(ident, constExpNodes, line);
        } else {
            funcFParamNode = new FuncFParamNode(ident, line);
        }

        printGrammaticalData("FuncFParam");

        currentSymbolTable.addVar(funcFParamNode.getIdent(), funcFParamNode);

        return funcFParamNode;
    }

    private DeclNode decl() {

        DeclNode declNode = null;
        if (isConstDecl()) {
            ConstDeclNode constDeclNode = constDecl();
            declNode = new DeclNode(constDeclNode, line);
        } else if (isVarDecl()) {
            VarDeclNode varDeclNode = varDecl();
            declNode = new DeclNode(varDeclNode, line);
        }
        return declNode;
    }

    private ConstDeclNode constDecl() {

        printWord(words.get(index));
        printWord(words.get(index));

        ArrayList<ConstDefNode> constDefNodes = new ArrayList<>();
        ConstDefNode constDefNode = constDef();
        constDefNodes.add(constDefNode);

        while (words.get(index).isComma()) {
            printWord(words.get(index));

            constDefNode = constDef();
            constDefNodes.add(constDefNode);
        }

        if (words.get(index).isSemicn()) {
            printWord(words.get(index));
        } else {
            Node.errors.add(new Pair<>(constDefNodes.get(constDefNodes.size() - 1).getLine(), SysYException.ExceptionCode.i));
        }

        ConstDeclNode constDeclNode = new ConstDeclNode(constDefNodes, line);

        printGrammaticalData("ConstDecl");

        return constDeclNode;
    }

    private ConstDefNode constDef() {

        Word ident = words.get(index);
        printWord(ident);

        ArrayList<ConstExpNode> constExpNodes = new ArrayList<>();
        while (words.get(index).isLbrack()) {
            printWord(words.get(index));

            ConstExpNode constExpNode = constExp();
            constExpNodes.add(constExpNode);

            if (words.get(index).isRbrack()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.k));
            }
        }

        printWord(words.get(index));

        ConstInitValNode constInitvalNode = constInitVal();

        ConstDefNode constDefNode = new ConstDefNode(ident, constExpNodes, constInitvalNode, line);

        printGrammaticalData("ConstDef");

        currentSymbolTable.addVar(constDefNode.getIdent(), constDefNode);

        return constDefNode;
    }

    private ConstInitValNode constInitVal() {
        ConstInitValNode constInitvalNode = null;

        if (words.get(index).isLbrace()) {
            printWord(words.get(index));

            if (words.get(index).isRbrace()) {

                printWord(words.get(index));

                constInitvalNode = new ConstInitValNode(line);

            } else {
                ArrayList<ConstInitValNode> constInitValNodes = new ArrayList<>();
                ConstInitValNode node = constInitVal();
                constInitValNodes.add(node);

                while (words.get(index).isComma()) {
                    printWord(words.get(index));

                    constInitvalNode = constInitVal();
                    constInitValNodes.add(constInitvalNode);
                }

                printWord(words.get(index));

                constInitvalNode = new ConstInitValNode(constInitValNodes, line);
            }

        } else {
            ConstExpNode constExpNode = constExp();

            constInitvalNode = new ConstInitValNode(constExpNode, line);
        }

        printGrammaticalData("ConstInitVal");

        return constInitvalNode;
    }

    private ConstExpNode constExp() {

        AddExpNode addExpNode = addExp();
        ConstExpNode constExpNode = new ConstExpNode(addExpNode, line);

        printGrammaticalData("ConstExp");

        return constExpNode;
    }

    private AddExpNode addExp() {

        MulExpNode mulExpNode = mulExp();
        AddExpNode addExpNode = new AddExpNode(mulExpNode, line);

        while (words.get(index).isPlus() || words.get(index).isMinu()) {
            printGrammaticalData("AddExp");

            Word separator = words.get(index);
            printWord(separator);

            mulExpNode = mulExp();
            addExpNode = new AddExpNode(addExpNode, separator, mulExpNode, line);
        }

        printGrammaticalData("AddExp");

        return addExpNode;
    }

    private MulExpNode mulExp() {

        UnaryExpNode unaryExpNode = unaryExp();


        MulExpNode mulExpNode = new MulExpNode(unaryExpNode, line);

        while (words.get(index).isMult() ||
                words.get(index).isDiv() ||
                words.get(index).isMod()) {
            printGrammaticalData("MulExp");

            Word separator = words.get(index);
            printWord(separator);

            unaryExpNode = unaryExp();

            mulExpNode = new MulExpNode(mulExpNode, separator, unaryExpNode, line);
        }

        printGrammaticalData("MulExp");

        return mulExpNode;
    }

    private UnaryExpNode unaryExp() {
        UnaryExpNode unaryExpNode = null;

        if (isUnaryOp()) {

            UnaryOpNode unaryOpNode = unaryOp();
            UnaryExpNode node = unaryExp();

            unaryExpNode = new UnaryExpNode(unaryOpNode, node, line);

        } else if (words.get(index).isIdent() && words.get(index + 1).isLparent()) {

            Word ident = words.get(index);
            printWord(ident);

            printWord(words.get(index));

            if (isFuncRParams()) {
                FuncRParamsNode funcRParamsNode = funcRParams();

                printWord(words.get(index));

                unaryExpNode = new UnaryExpNode(ident, funcRParamsNode, line);
            } else {
                if (words.get(index).isRParent()) {
                    printWord(words.get(index));

                    unaryExpNode = new UnaryExpNode(ident, line);
                } else {
                    Node.errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.j));
                    unaryExpNode = new UnaryExpNode(ident, line);
                }
            }

        } else {
            PrimaryExpNode primaryExpNode = primaryExp();
            unaryExpNode = new UnaryExpNode(primaryExpNode, line);
        }

        printGrammaticalData("UnaryExp");

        if (!unaryExpNode.checkErrorC(currentSymbolTable)) {
            if (!unaryExpNode.checkErrorD(currentSymbolTable)) {
                    unaryExpNode.checkErrorE(currentSymbolTable);
            }
        }

        return unaryExpNode;
    }

    private UnaryOpNode unaryOp() {

        Word separator = words.get(index);
        printWord(separator);

        UnaryOpNode unaryOpNode = new UnaryOpNode(separator, line);

        printGrammaticalData("UnaryOp");

        return unaryOpNode;
    }

    private FuncRParamsNode funcRParams() {

        ArrayList<ExpNode> expNodes = new ArrayList<>();

        ExpNode expNode = exp();
        expNodes.add(expNode);
        while (words.get(index).isComma()) {
            printWord(words.get(index));

            expNode = exp();
            expNodes.add(expNode);

        }

        FuncRParamsNode funcRParamsNode = new FuncRParamsNode(expNodes, line);

        printGrammaticalData("FuncRParams");

        return funcRParamsNode;
    }

    private PrimaryExpNode primaryExp() {

        PrimaryExpNode primaryExpNode = null;
        if (words.get(index).isLparent()) {
            printWord(words.get(index));

            ExpNode expNode = exp();
            primaryExpNode = new PrimaryExpNode(expNode, line);

            printWord(words.get(index));

        } else if (words.get(index).isIdent()) {

            LValNode lValNode = lVal();

            primaryExpNode = new PrimaryExpNode(lValNode, line);

        } else if (words.get(index).isIntConst()) {

            NumberNode numberNode = number();
            primaryExpNode = new PrimaryExpNode(numberNode, line);

        }

        printGrammaticalData("PrimaryExp");

        return primaryExpNode;
    }

    private ExpNode exp() {

        AddExpNode addExpNode = addExp();
        ExpNode expNode = new ExpNode(addExpNode, line);

        printGrammaticalData("Exp");

        return expNode;
    }

    private LValNode lVal() {

        Word ident = words.get(index);
        printWord(ident);

        ArrayList<ExpNode> expNodes = new ArrayList<>();
        while (words.get(index).isLbrack()) {
            printWord(words.get(index));

            ExpNode expNode = exp();
            expNodes.add(expNode);

            if (words.get(index).isRbrack()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.k));
            }
        }

        LValNode lValNode = new LValNode(ident, expNodes, line);

        printGrammaticalData("LVal");

        lValNode.checkErrorC(currentSymbolTable);

        return lValNode;
    }

    private NumberNode number() {

        Word intConst = words.get(index);
        printWord(intConst);

        NumberNode numberNode = new NumberNode(intConst, line);

        printGrammaticalData("Number");

        return numberNode;
    }

    private VarDeclNode varDecl() {

        printWord(words.get(index));

        ArrayList<VarDefNode> varDefNodes = new ArrayList<>();

        int varLine = line;
        VarDefNode varDefNode = varDef();
        varDefNodes.add(varDefNode);

        while (words.get(index).isComma()) {
            printWord(words.get(index));

            varDefNode = varDef();
            varDefNodes.add(varDefNode);
        }

        // ';'
        if (words.get(index).isSemicn()) {
            printWord(words.get(index));
        } else {
            Node.errors.add(new Pair<>(varLine, SysYException.ExceptionCode.i));
        }

        VarDeclNode varDeclNode = new VarDeclNode(varDefNodes, line);

        printGrammaticalData("VarDecl");

        return varDeclNode;
    }

    private VarDefNode varDef() {

        Word ident = words.get(index);
        printWord(ident);

        VarDefNode varDefNode = null;

        ArrayList<ConstExpNode> constExpNodes = new ArrayList<>();
        while (words.get(index).isLbrack()) {
            printWord(words.get(index));

            ConstExpNode constExpNode = constExp();
            constExpNodes.add(constExpNode);

            if (words.get(index).isRbrack()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(ident.getLineNumber(), SysYException.ExceptionCode.k));
            }
        }

        if (words.get(index).isAssign()) {
            printWord(words.get(index));

            InitValNode initValNode = initVal();
            varDefNode = new VarDefNode(ident, constExpNodes, initValNode, line);

        } else {
            varDefNode = new VarDefNode(ident, constExpNodes, line);
        }

        printGrammaticalData("VarDef");

        currentSymbolTable.addVar(varDefNode.getIdent(), varDefNode);

        return varDefNode;
    }

    private InitValNode initVal() {

        InitValNode initValNode = null;

        if (words.get(index).isLbrace()) {
            printWord(words.get(index));

            if (words.get(index).isRbrace()) {
                printWord(words.get(index));

                initValNode = new InitValNode(line);
            } else {
                ArrayList<InitValNode> nodes = new ArrayList<>();
                InitValNode node = initVal();
                nodes.add(node);

                while (words.get(index).isComma()) {
                    printWord(words.get(index));

                    node = initVal();
                    nodes.add(node);
                }

                printWord(words.get(index));

                initValNode = new InitValNode(nodes, line);
            }
        } else {
            ExpNode expNode = exp();
            initValNode = new InitValNode(expNode, line);
        }

        printGrammaticalData("InitVal");

        return initValNode;
    }

    private MainFuncDefNode mainFuncDef() {
        currentSymbolTable.toChild();

        printWord(words.get(index));

        Word ident = words.get(index);
        printWord(ident);

        Word separator = words.get(index);
        printWord(separator);

        if (words.get(index).isRParent()) {
            printWord(words.get(index));
        } else {
            Node.errors.add(new Pair<>(separator.getLineNumber(), SysYException.ExceptionCode.j));
        }

        BlockNode blockNode = block();
        MainFuncDefNode mainFuncDefNode = new MainFuncDefNode(ident, blockNode, line);

        printGrammaticalData("MainFuncDef");

        if (!mainFuncDefNode.checkErrorB(currentSymbolTable)) {
            currentSymbolTable.addVar(mainFuncDefNode.getIdent(), mainFuncDefNode);
        }

        mainFuncDefNode.checkErrorG(currentSymbolTable);

        return mainFuncDefNode;
    }

    private BlockNode block() {
        printWord(words.get(index));

        ArrayList<BlockItemNode> blockItemNodes = new ArrayList<>();

        while (!words.get(index).isRbrace()) {
            BlockItemNode blockItemNode = blockItem();
            blockItemNodes.add(blockItemNode);
        }

        int endLine = words.get(index).getLineNumber();
        printWord(words.get(index));

        BlockNode blockNode = new BlockNode(blockItemNodes, endLine, line, currentSymbolTable.yield());

        printGrammaticalData("Block");

        currentSymbolTable.back();

        return blockNode;
    }

    private BlockItemNode blockItem() {

        BlockItemNode blockItemNode = null;

        if (isDecl()) {

            DeclNode declNode = decl();
            blockItemNode = new BlockItemNode(declNode, line);

        } else {

            StmtNode stmtNode = stmt();
            blockItemNode = new BlockItemNode(stmtNode, line);

        }

        return blockItemNode;
    }

    private StmtNode stmt() {

        StmtNode stmtNode = null;

        if (words.get(index).isPrintf()) {

            int printfLine = line;
            printWord(words.get(index));
            printWord(words.get(index));

            // String
            Word formatString = words.get(index);
            printWord(formatString);

            ArrayList<ExpNode> expNodes = new ArrayList<>();

            while (words.get(index).isComma()) {
                printWord(words.get(index));

                ExpNode expNode = exp();
                expNodes.add(expNode);
            }

            if (words.get(index).isRParent()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(printfLine, SysYException.ExceptionCode.j));
            }

            if (words.get(index).isSemicn()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(printfLine, SysYException.ExceptionCode.i));
            }

            stmtNode = new StmtPrintfNode(formatString, expNodes, printfLine);
        } else if (words.get(index).isReturn()) {
            Word reserved = words.get(index);
            printWord(reserved);

            if (words.get(index).isSemicn()) {
                printWord(words.get(index));

                stmtNode = new StmtReturnNode(reserved.getLineNumber());

            } else if (isExp()) {
                int expLine = line;
                ExpNode expNode = exp();

                if (words.get(index).isSemicn()) {
                    printWord(words.get(index));
                } else {
                    Node.errors.add(new Pair<>(expLine, SysYException.ExceptionCode.i));
                }

                stmtNode = new StmtReturnNode(expNode, reserved.getLineNumber());
            } else {
                Node.errors.add(new Pair<>(reserved.getLineNumber(), SysYException.ExceptionCode.i));
                stmtNode = new StmtReturnNode(null, reserved.getLineNumber());
            }
        } else if (words.get(index).isBreak()) {

            int breakLine = line;

            printWord(words.get(index));

            if (words.get(index).isSemicn()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(breakLine, SysYException.ExceptionCode.i));
            }

            stmtNode = new StmtBreakNode(breakLine);
        } else if (words.get(index).isContinue()) {
            int continueLine = line;

            printWord(words.get(index));

            if (words.get(index).isSemicn()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(continueLine, SysYException.ExceptionCode.i));
            }

            stmtNode = new StmtContinueNode(continueLine);
        } else if (words.get(index).isWhile()) {
            int whileLine = line;

            printWord(words.get(index));
            printWord(words.get(index));

            CondNode condNode = cond();

            if (words.get(index).isRParent()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(whileLine, SysYException.ExceptionCode.j));
            }

            currentSymbolTable.setLoop(currentSymbolTable.getLoop() + 1);

            StmtNode node = stmt();

            stmtNode = new StmtLoopNode(condNode, node, line);
        } else if (words.get(index).isIf()) {
            int ifLine = line;
            printWord(words.get(index));
            printWord(words.get(index));

            CondNode condNode = cond();

            if (words.get(index).isRParent()) {
                printWord(words.get(index));
            } else {
                Node.errors.add(new Pair<>(ifLine, SysYException.ExceptionCode.j));
            }

            StmtNode node = stmt();

            if (words.get(index).isElse()) {
                printWord(words.get(index));

                StmtNode node2 = stmt();
                stmtNode = new StmtBranchNode(condNode, node, node2, line);
            } else {
                stmtNode = new StmtBranchNode(condNode, node, line);
            }
        } else if (words.get(index).isLbrace()) {
            currentSymbolTable.toChild();

            BlockNode blockNode = block();

            stmtNode = new StmtBlockNode(blockNode, line);

        } else {
            int opt = index;
            boolean flag = false;
            if (words.get(opt).isIdent() &&
                !words.get(opt + 1).isLparent()) {
                while (words.get(opt + 1).isLbrack()) {
                    opt++;
                    while (!words.get(opt).isRbrack()) {
                        opt++;
                    }
                }
                if (words.get(opt + 1).isAssign()) {
                    flag = true;
                }
            }
            if (flag) {
                LValNode lValNode = lVal();

                printWord(words.get(index));

                if (words.get(index).isGetint()) {
                    int getintLine = line;
                    printWord(words.get(index));
                    printWord(words.get(index));

                    if (words.get(index).isRParent()) {
                        printWord(words.get(index));
                    } else {
                        Node.errors.add(new Pair<>(getintLine, SysYException.ExceptionCode.j));
                    }

                    if (words.get(index).isSemicn()) {
                        printWord(words.get(index));
                    } else {
                        Node.errors.add(new Pair<>(getintLine, SysYException.ExceptionCode.i));
                    }

                    stmtNode = new StmtGetIntNode(lValNode, line);
                } else {
                    int expLine = line;
                    ExpNode expNode = exp();

                    if (words.get(index).isSemicn()) {
                        printWord(words.get(index));
                    } else {
                        Node.errors.add(new Pair<>(expLine, SysYException.ExceptionCode.i));
                    }

                    stmtNode = new StmtAssignNode(lValNode, expNode, line);
                }
            } else {
                if (words.get(index).isSemicn()) {

                    printWord(words.get(index));

                    stmtNode = new StmtExprNode(line);
                } else {
                    int nowLine = line;
                    if (isExp()) {
                        int expLine = line;

                        ExpNode expNode = exp();

                        if (words.get(index).isSemicn()) {
                            printWord(words.get(index));
                        } else {
                            Node.errors.add(new Pair<>(expLine, SysYException.ExceptionCode.i));
                        }

                        stmtNode = new StmtExprNode(expNode, line);
                    } else {
                        Node.errors.add(new Pair<>(nowLine, SysYException.ExceptionCode.i));
                    }
                }
            }
        }

        printGrammaticalData("Stmt");

        if (stmtNode instanceof StmtAssignNode) {
            ((StmtAssignNode) stmtNode).checkErrorH(currentSymbolTable);
        } else if (stmtNode instanceof StmtGetIntNode) {
            ((StmtGetIntNode) stmtNode).checkErrorH(currentSymbolTable);
        }

        if (stmtNode instanceof StmtBreakNode) {
            ((StmtBreakNode) stmtNode).checkErrorM(currentSymbolTable);
        } else if (stmtNode instanceof StmtContinueNode) {
            ((StmtContinueNode) stmtNode).checkErrorM(currentSymbolTable);
        }

        if (stmtNode instanceof StmtLoopNode && currentSymbolTable.getLoop() > 0) {
            currentSymbolTable.setLoop(currentSymbolTable.getLoop() - 1);
        }

        return stmtNode;
    }

    private CondNode cond() {

        LOrExpNode lOrExpNode = lOrExp();

        CondNode condNode = new CondNode(lOrExpNode, line);

        printGrammaticalData("Cond");

        return condNode;
    }

    private LOrExpNode lOrExp() {

        LAndExpNode lAndExpNode = lAndExp();
        LOrExpNode lOrExpNode = new LOrExpNode(lAndExpNode, line);

        while (words.get(index).isOr()) {
            printGrammaticalData("LOrExp");

            printWord(words.get(index));

            lAndExpNode = lAndExp();
            lOrExpNode = new LOrExpNode(lOrExpNode, lAndExpNode, line);
        }

        printGrammaticalData("LOrExp");

        return lOrExpNode;

    }

    private LAndExpNode lAndExp() {

        EqExpNode eqExpNode = eqExp();
        LAndExpNode lAndExpNode = new LAndExpNode(eqExpNode, line);

        while (words.get(index).isAnd()) {
            printGrammaticalData("LAndExp");

            printWord(words.get(index));

            eqExpNode = eqExp();
            lAndExpNode = new LAndExpNode(lAndExpNode, eqExpNode, line);
        }

        printGrammaticalData("LAndExp");

        return lAndExpNode;

    }

    private EqExpNode eqExp() {

        RelExpNode relExpNode = relExp();
        EqExpNode eqExpNode = new EqExpNode(relExpNode, line);

        while (words.get(index).isEql() || words.get(index).isNeq()) {
            printGrammaticalData("EqExp");

            Word separator = words.get(index);
            printWord(separator);

            relExpNode = relExp();
            eqExpNode = new EqExpNode(eqExpNode, separator, relExpNode, line);
        }

        printGrammaticalData("EqExp");

        return eqExpNode;

    }

    private RelExpNode relExp() {

        AddExpNode addExpNode = addExp();
        RelExpNode relExpNode = new RelExpNode(addExpNode, line);

        while (words.get(index).isLss() ||
                words.get(index).isLeq() ||
                words.get(index).isGre() ||
                words.get(index).isGeq()) {
            printGrammaticalData("RelExp");

            Word separator = words.get(index);
            printWord(separator);

            addExpNode = addExp();
            relExpNode = new RelExpNode(relExpNode, separator, addExpNode, line);
        }

        printGrammaticalData("RelExp");

        return relExpNode;
    }

    private boolean isUnaryOp() {
        if (words.get(index).isPlus() ||
                words.get(index).isMinu() ||
                words.get(index).isNot()) {
            return true;
        }
        return false;
    }

    private boolean isDecl() {
        if (isConstDecl() || isVarDecl()) {
            return true;
        }
        return false;
    }

    private boolean isConstDecl() {
        if (words.get(index).isConst()) {
            return true;
        }
        return false;
    }

    private boolean isVarDecl() {
        if (words.get(index).isInt() &&
                words.get(index + 1).isIdent() &&
                (words.get(index + 2).isLbrack()
                        || words.get(index + 2).isAssign()
                        || words.get(index + 2).isSemicn()
                        || words.get(index + 2).isComma())) {
            return true;
        }
        // 分号缺失
        if (words.get(index).isInt() &&
                words.get(index + 1).isIdent() &&
                (words.get(index + 2).isInt()
                        || words.get(index + 2).isVoid())) {
            return true;
        }
        return false;
    }

    private boolean isFuncDef() {
        if ((words.get(index).isVoid() || words.get(index).isInt()) &&
                words.get(index + 1).isIdent()) {
            return true;
        }
        return false;
    }

    private boolean isExp() {
        if (words.get(index).isLparent() ||
                words.get(index).isIntConst() ||
                words.get(index).isIdent() ||
                words.get(index).isPlus() ||
                words.get(index).isMinu() ||
                words.get(index).isNot()) {
            return true;
        }
        return false;
    }

    private boolean isFuncFParams() {
        if (words.get(index).isInt()) {
            return true;
        }
        return false;
    }

    private boolean isFuncRParams() {
        return isExp();
    }

    private void printWord(Word word) {
        if (LexicalAnalyzer.printLexicalAnalyzerData) {
            System.out.println(word.getCategoryCode() + " " + word.getWordValue());
        }
        index++;
        if (index < wordsLength) {
            line = words.get(index).getLineNumber();
//            System.out.println(line);
        }
    }

    private void printGrammaticalData(String name) {
        if (printGrammaticalAnalyzerData) {
            System.out.println("<" + name + ">");
        }
    }

}
