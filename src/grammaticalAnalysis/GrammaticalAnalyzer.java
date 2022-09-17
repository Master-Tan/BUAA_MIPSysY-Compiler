package grammaticalAnalysis;

import grammaticalAnalysis.grammatical.*;
import lexicalAnalysis.LexicalAnalyzer;
import lexicalAnalysis.lexical.Word;

import java.util.ArrayList;

public class GrammaticalAnalyzer {

    public static final boolean printGrammaticalAnalyzerData = true;  // 是否输出语法分析结果

    private ArrayList<Word> words;
    private int index;
    private int wordsLength;

    public GrammaticalAnalyzer(ArrayList<Word> words) {

        this.words = words;
        this.index = 0;
        this.wordsLength = this.words.size();

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
        CompUnitNode compUnitNode = new CompUnitNode(declNodes, funcDefNodes, mainFuncDefNode);

        printGrammaticalData("CompUnit");

        return compUnitNode;
    }

    private FuncDefNode funcDef() {

        FuncDefNode funcDefNode = null;

        FuncTypeNode funcTypeNode = funcType();

        Word ident = words.get(index);
        printWord(ident);
        index++;

        printWord(words.get(index));
        index++;

        if (words.get(index).isRParent()) {
            printWord(words.get(index));
            index++;

            BlockNode blockNode = block();

            funcDefNode = new FuncDefNode(funcTypeNode, ident, blockNode);
        } else {
            FuncFParamsNode funcFParamsNode = funcFParams();

            printWord(words.get(index));
            index++;

            BlockNode blockNode = block();

            funcDefNode = new FuncDefNode(funcTypeNode, ident, funcFParamsNode, blockNode);
        }

        printGrammaticalData("FuncDef");

        return funcDefNode;
    }

    private FuncTypeNode funcType() {

        Word reserved = words.get(index);
        printWord(reserved);
        index++;

        FuncTypeNode funcTypeNode = new FuncTypeNode(reserved);

        printGrammaticalData("FuncType");

        return funcTypeNode;
    }

    private FuncFParamsNode funcFParams() {

        ArrayList<FuncFParamNode> funcFParamNodes = new ArrayList<>();
        FuncFParamNode funcFParamNode = funcFParam();
        funcFParamNodes.add(funcFParamNode);

        while (words.get(index).isComma()) {
            printWord(words.get(index));
            index++;

            funcFParamNode = funcFParam();
            funcFParamNodes.add(funcFParamNode);
        }

        FuncFParamsNode funcFParamsNode = new FuncFParamsNode(funcFParamNodes);

        printGrammaticalData("FuncFParams");

        return funcFParamsNode;
    }

    private FuncFParamNode funcFParam() {

        FuncFParamNode funcFParamNode = null;

        printWord(words.get(index));
        index++;

        Word ident = words.get(index);
        printWord(ident);
        index++;

        if (words.get(index).isLbrack()) {

            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            ArrayList<ConstExpNode> constExpNodes = new ArrayList<>();
            while (words.get(index).isLbrack()) {
                printWord(words.get(index));
                index++;

                ConstExpNode constExpNode = constExp();
                constExpNodes.add(constExpNode);

                printWord(words.get(index));
                index++;
            }

            funcFParamNode = new FuncFParamNode(ident, constExpNodes);
        } else {
            funcFParamNode = new FuncFParamNode(ident);
        }

        printGrammaticalData("FuncFParams");

        return funcFParamNode;
    }

    private DeclNode decl() {

        DeclNode declNode = null;
        if (isConstDecl()) {
            ConstDeclNode constDeclNode = constDecl();
            declNode = new DeclNode(constDeclNode);
        } else if (isVarDecl()) {
            VarDeclNode varDeclNode = varDecl();
            declNode = new DeclNode(varDeclNode);
        }
        return declNode;
    }

    private ConstDeclNode constDecl() {

        printWord(words.get(index));
        index++;
        printWord(words.get(index));
        index++;

        ArrayList<ConstDefNode> constDefNodes = new ArrayList<>();
        ConstDefNode constDefNode = constDef();
        constDefNodes.add(constDefNode);
        while (words.get(index).isComma()) {
            printWord(words.get(index));
            index++;

            constDefNode = constDef();
            constDefNodes.add(constDefNode);
        }

        printWord(words.get(index));
        index++;

        ConstDeclNode constDeclNode = new ConstDeclNode(constDefNodes);

        printGrammaticalData("ConstDecl");

        return constDeclNode;
    }

    private ConstDefNode constDef() {

        Word ident = words.get(index);
        printWord(ident);
        index++;

        ArrayList<ConstExpNode> constExpNodes = new ArrayList<>();
        while (words.get(index).isLbrack()) {
            printWord(words.get(index));
            index++;

            ConstExpNode constExpNode = constExp();
            constExpNodes.add(constExpNode);

            printWord(words.get(index));
            index++;
        }

        printWord(words.get(index));
        index++;

        ConstInitValNode constInitvalNode = constInitVal();

        ConstDefNode constDefNode = new ConstDefNode(ident, constExpNodes, constInitvalNode);

        printGrammaticalData("ConstDef");

        return constDefNode;
    }

    private ConstInitValNode constInitVal() {
        ConstInitValNode constInitvalNode = null;

        if (words.get(index).isLbrace()) {
            printWord(words.get(index));
            index++;

            ArrayList<ConstInitValNode> constInitValNodes = new ArrayList<>();
            ConstInitValNode node = constInitVal();
            constInitValNodes.add(node);

            while (words.get(index).isComma()) {
                printWord(words.get(index));
                index++;

                constInitvalNode = constInitVal();
                constInitValNodes.add(constInitvalNode);
            }

            printWord(words.get(index));
            index++;

            constInitvalNode = new ConstInitValNode(constInitValNodes);
        } else {
            ConstExpNode constExpNode = constExp();

            constInitvalNode = new ConstInitValNode(constExpNode);
        }

        printGrammaticalData("ConstInitVal");

        return constInitvalNode;
    }

    private ConstExpNode constExp() {

        AddExpNode addExpNode = addExp();
        ConstExpNode constExpNode = new ConstExpNode(addExpNode);

        return constExpNode;
    }

    private AddExpNode addExp() {

        MulExpNode mulExpNode = mulExp();
        AddExpNode addExpNode = new AddExpNode(mulExpNode);

        while (words.get(index).isPlus() || words.get(index).isMinu()) {
            printGrammaticalData("AddExp");

            Word separator = words.get(index);
            printWord(separator);
            index++;

            mulExpNode = mulExp();
            addExpNode = new AddExpNode(addExpNode, separator, mulExpNode);
        }

        printGrammaticalData("AddExp");

        return addExpNode;
    }

    private MulExpNode mulExp() {

        UnaryExpNode unaryExpNode = unaryExp();
        MulExpNode mulExpNode = new MulExpNode(unaryExpNode);

        while (words.get(index).isMult() ||
                words.get(index).isDiv() ||
                words.get(index).isMod()) {
            printGrammaticalData("MulExp");

            Word separator = words.get(index);
            printWord(separator);
            index++;

            unaryExpNode = unaryExp();
            mulExpNode = new MulExpNode(mulExpNode, separator, unaryExpNode);
        }

        printGrammaticalData("MulExp");

        return mulExpNode;
    }

    private UnaryExpNode unaryExp() {
        UnaryExpNode unaryExpNode = null;

        if (isUnaryOp()) {

            UnaryOpNode unaryOpNode = unaryOp();
            UnaryExpNode node = unaryExp();

            unaryExpNode = new UnaryExpNode(unaryOpNode, node);

        } else if (words.get(index).isIdent() && words.get(index + 1).isLparent()) {

            Word ident = words.get(index);
            printWord(ident);
            index++;

            printWord(words.get(index));
            index++;

            if (words.get(index).isRParent()) {
                printWord(words.get(index));
                index++;

                unaryExpNode = new UnaryExpNode(ident);
            } else {
                FuncRParamsNode funcRParamsNode = funcRParams();

                printWord(words.get(index));
                index++;

                unaryExpNode = new UnaryExpNode(ident, funcRParamsNode);
            }

        } else {
            PrimaryExpNode primaryExpNode = primaryExp();
            unaryExpNode = new UnaryExpNode(primaryExpNode);
        }

        printGrammaticalData("UnaryExp");

        return unaryExpNode;
    }

    private UnaryOpNode unaryOp() {

        Word separator = words.get(index);
        printWord(separator);
        index++;

        UnaryOpNode unaryOpNode = new UnaryOpNode(separator);

        printGrammaticalData("UnaryOp");

        return unaryOpNode;
    }

    private FuncRParamsNode funcRParams() {

        ArrayList<ExpNode> expNodes = new ArrayList<>();

        ExpNode expNode = exp();
        expNodes.add(expNode);
        while (words.get(index).isComma()) {
            printWord(words.get(index));
            index++;

            expNode = exp();
            expNodes.add(expNode);

        }

        FuncRParamsNode funcRParamsNode = new FuncRParamsNode(expNodes);

        printGrammaticalData("FuncRParams");

        return funcRParamsNode;
    }

    private PrimaryExpNode primaryExp() {

        PrimaryExpNode primaryExpNode = null;
        if (words.get(index).isLparent()) {
            printWord(words.get(index));
            index++;

            ExpNode expNode = exp();
            primaryExpNode = new PrimaryExpNode(expNode);

            printWord(words.get(index));
            index++;

        } else if (words.get(index).isIdent()) {

            LValNode lValNode = lVal();
            primaryExpNode = new PrimaryExpNode(lValNode);

        } else if (words.get(index).isIntConst()) {

            NumberNode numberNode = number();
            primaryExpNode = new PrimaryExpNode(numberNode);

        }

        printGrammaticalData("PrimaryExp");

        return primaryExpNode;
    }

    private ExpNode exp() {

        AddExpNode addExpNode = addExp();
        ExpNode expNode = new ExpNode(addExpNode);

        printGrammaticalData("Exp");

        return expNode;
    }

    private LValNode lVal() {

        Word ident = words.get(index);
        printWord(ident);
        index++;

        ArrayList<ExpNode> expNodes = new ArrayList<>();
        while (words.get(index).isLbrack()) {
            printWord(words.get(index));
            index++;

            ExpNode expNode = exp();
            expNodes.add(expNode);

            printWord(words.get(index));
            index++;
        }

        LValNode lValNode = new LValNode(ident, expNodes);

        printGrammaticalData("LVal");

        return lValNode;
    }

    private NumberNode number() {

        Word intConst = words.get(index);
        printWord(intConst);
        index++;

        NumberNode numberNode = new NumberNode(intConst);

        printGrammaticalData("Number");

        return numberNode;
    }

    private VarDeclNode varDecl() {

        printWord(words.get(index));
        index++;

        ArrayList<VarDefNode> varDefNodes = new ArrayList<>();
        VarDefNode varDefNode = varDef();
        varDefNodes.add(varDefNode);
        while (words.get(index).isComma()) {
            printWord(words.get(index));
            index++;

            varDefNode = varDef();
            varDefNodes.add(varDefNode);
        }

        // ';'
        printWord(words.get(index));
        index++;

        VarDeclNode varDeclNode = new VarDeclNode(varDefNodes);

        printGrammaticalData("VarDecl");

        return varDeclNode;
    }

    private VarDefNode varDef() {

        Word ident = words.get(index);
        printWord(ident);
        index++;

        VarDefNode varDefNode = null;

        ArrayList<ConstExpNode> constExpNodes = new ArrayList<>();
        while (words.get(index).isLbrack()) {
            printWord(words.get(index));
            index++;

            ConstExpNode constExpNode = constExp();
            constExpNodes.add(constExpNode);

            printWord(words.get(index));
            index++;
        }

        if (words.get(index).isAssign()) {
            printWord(words.get(index));
            index++;

            InitValNode initValNode = initVal();
            varDefNode = new VarDefNode(constExpNodes, initValNode);

        } else {
            varDefNode = new VarDefNode(constExpNodes);
        }

        printGrammaticalData("VarDef");

        return varDefNode;
    }

    private InitValNode initVal() {

        InitValNode initValNode = null;

        if (words.get(index).isLbrace()) {
            printWord(words.get(index));
            index++;
            if (words.get(index).isRbrace()) {
                printWord(words.get(index));
                index++;
                initValNode = new InitValNode();
            } else {
                ArrayList<InitValNode> nodes = new ArrayList<>();
                InitValNode node = initVal();
                nodes.add(node);

                while (words.get(index).isComma()) {
                    printWord(words.get(index));
                    index++;

                    node = initVal();
                    nodes.add(node);
                }

                printWord(words.get(index));
                index++;
                initValNode = new InitValNode(nodes);
            }
        } else {
            ExpNode expNode = exp();
            initValNode = new InitValNode(expNode);
        }

        printGrammaticalData("InitVal");

        return initValNode;
    }

    private MainFuncDefNode mainFuncDef() {

        printWord(words.get(index));
        index++;
        printWord(words.get(index));
        index++;
        printWord(words.get(index));
        index++;
        printWord(words.get(index));
        index++;

        BlockNode blockNode = block();
        MainFuncDefNode mainFuncDefNode = new MainFuncDefNode(blockNode);

        printGrammaticalData("MainFuncDef");

        return mainFuncDefNode;
    }

    private BlockNode block() {
        printWord(words.get(index));
        index++;

        ArrayList<BlockItemNode> blockItemNodes = new ArrayList<>();

        while (!words.get(index).isRbrace()) {
            BlockItemNode blockItemNode = blockItem();
            blockItemNodes.add(blockItemNode);

        }

        printWord(words.get(index));
        index++;

        BlockNode blockNode = new BlockNode(blockItemNodes);

        printGrammaticalData("Block");

        return blockNode;
    }

    private BlockItemNode blockItem() {

        BlockItemNode blockItemNode = null;

        if (isDecl()) {

            DeclNode declNode = decl();
            blockItemNode = new BlockItemNode(declNode);

        } else {

            StmtNode stmtNode = stmt();
            blockItemNode = new BlockItemNode(stmtNode);

        }

        return blockItemNode;
    }

    private StmtNode stmt() {

        StmtNode stmtNode = null;

        if (words.get(index).isPrintf()) {
            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            // String
            Word formatString = words.get(index);
            printWord(formatString);
            index++;

            ArrayList<ExpNode> expNodes = new ArrayList<>();

            while (words.get(index).isComma()) {
                printWord(words.get(index));
                index++;

                ExpNode expNode = exp();
                expNodes.add(expNode);
            }

            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            stmtNode = new StmtPrintfNode(formatString, expNodes);
        } else if (words.get(index).isReturn()) {
            printWord(words.get(index));
            index++;

            if (words.get(index).isRbrack()) {
                printWord(words.get(index));
                index++;

                stmtNode = new StmtReturnNode();
            } else {
                ExpNode expNode = exp();

                printWord(words.get(index));
                index++;

                stmtNode = new StmtReturnNode(expNode);
            }
        } else if (words.get(index).isBreak()) {

            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            stmtNode = new StmtBreakNode();
        } else if (words.get(index).isContinue()) {

            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            stmtNode = new StmtContinueNode();
        } else if (words.get(index).isWhile()) {
            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            CondNode condNode = cond();

            printWord(words.get(index));
            index++;

            StmtNode node = stmt();

            stmtNode = new StmtLoopNode(condNode, node);
        } else if (words.get(index).isIf()) {
            printWord(words.get(index));
            index++;
            printWord(words.get(index));
            index++;

            CondNode condNode = cond();

            printWord(words.get(index));
            index++;

            StmtNode node = stmt();

            if (words.get(index).isElse()) {
                printWord(words.get(index));
                index++;

                StmtNode node2 = stmt();
                stmtNode = new StmtBranchNode(condNode, node, node2);
            } else {
                stmtNode = new StmtBranchNode(condNode, node);
            }
        } else if (words.get(index).isLbrace()) {
            BlockNode blockNode = block();

            stmtNode = new StmtBlockNode(blockNode);
        } else {
            int opt = index;
            boolean flag = false;
            while (!words.get(opt).isSemicn()) {
                if (words.get(opt).isAssign()) {
                    flag = true;
                }
                opt++;
            }
            if (flag) {
                LValNode lValNode = lVal();

                printWord(words.get(index));
                index++;

                if (words.get(index).isGetint()) {
                    printWord(words.get(index));
                    index++;
                    printWord(words.get(index));
                    index++;
                    printWord(words.get(index));
                    index++;
                    printWord(words.get(index));
                    index++;

                    stmtNode = new StmtGetIntNode(lValNode);
                } else {
                    ExpNode expNode = exp();

                    printWord(words.get(index));
                    index++;

                    stmtNode = new StmtAssignNode(lValNode, expNode);
                }
            } else {
                if (words.get(index).isSemicn()) {

                    printWord(words.get(index));
                    index++;

                    stmtNode = new StmtExprNode();
                } else {
                    ExpNode expNode = exp();

                    printWord(words.get(index));
                    index++;

                    stmtNode = new StmtExprNode(expNode);
                }
            }
        }

        printGrammaticalData("Stmt");

        return stmtNode;
    }

    private CondNode cond() {

        LOrExpNode lOrExpNode = lOrExp();

        CondNode condNode = new CondNode(lOrExpNode);

        return condNode;
    }

    private LOrExpNode lOrExp() {

        LAndExpNode lAndExpNode = lAndExp();
        LOrExpNode lOrExpNode = new LOrExpNode(lAndExpNode);

        while (words.get(index).isOr()) {
            printGrammaticalData("LOrExp");

            printWord(words.get(index));
            index++;

            lAndExpNode = lAndExp();
            lOrExpNode = new LOrExpNode(lOrExpNode, lAndExpNode);
        }

        printGrammaticalData("LOrExp");

        return lOrExpNode;

    }

    private LAndExpNode lAndExp() {

        EqExpNode eqExpNode = eqExp();
        LAndExpNode lAndExpNode = new LAndExpNode(eqExpNode);

        while (words.get(index).isAnd()) {
            printGrammaticalData("LAndExp");

            printWord(words.get(index));
            index++;

            eqExpNode = eqExp();
            lAndExpNode = new LAndExpNode(lAndExpNode, eqExpNode);
        }

        printGrammaticalData("LAndExp");

        return lAndExpNode;

    }

    private EqExpNode eqExp() {

        RelExpNode relExpNode = relExp();
        EqExpNode eqExpNode = new EqExpNode(relExpNode);

        while (words.get(index).isEql() || words.get(index).isNeq()) {
            printGrammaticalData("EqExp");

            Word separator = words.get(index);
            printWord(separator);
            index++;

            relExpNode = relExp();
            eqExpNode = new EqExpNode(eqExpNode, separator, relExpNode);
        }

        printGrammaticalData("EqExp");

        return eqExpNode;

    }

    private RelExpNode relExp() {

        AddExpNode addExpNode = addExp();
        RelExpNode relExpNode = new RelExpNode(addExpNode);

        while (words.get(index).isLss() ||
                words.get(index).isLeq() ||
                words.get(index).isGre() ||
                words.get(index).isGeq()) {
            printGrammaticalData("RelExp");

            Word separator = words.get(index);
            printWord(separator);
            index++;

            addExpNode = addExp();
            relExpNode = new RelExpNode(relExpNode, separator, addExpNode);
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
        return false;
    }

    private boolean isFuncDef() {
        if ((words.get(index).isVoid() || words.get(index).isInt()) &&
                words.get(index + 1).isIdent()) {
            return true;
        }
        return false;
    }

    private void printWord(Word word) {
        if (LexicalAnalyzer.printLexicalAnalyzerData) {
            System.out.println(word.getCategoryCode() + " " + word.getWordValue());
        }
    }

    private void printGrammaticalData(String name) {
        if (printGrammaticalAnalyzerData) {
            System.out.println("<" + name + ">");
        }
    }

}
