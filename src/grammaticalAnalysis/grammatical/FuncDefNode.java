package grammaticalAnalysis.grammatical;

import lexicalAnalysis.lexical.Word;

public class FuncDefNode {

    private FuncTypeNode funcTypeNode;
    private Word ident;
    private FuncFParamsNode funcFParamsNode;
    private BlockNode blockNode;

    public FuncDefNode(FuncTypeNode funcTypeNode, Word ident, FuncFParamsNode funcFParamsNode, BlockNode blockNode) {
        this.funcTypeNode = funcTypeNode;
        this.ident = ident;
        this.funcFParamsNode = funcFParamsNode;
        this.blockNode = blockNode;
    }

    public FuncDefNode(FuncTypeNode funcTypeNode, Word ident, BlockNode blockNode) {
        this.funcTypeNode = funcTypeNode;
        this.ident = ident;
        this.funcFParamsNode = null;
        this.blockNode = blockNode;
    }

}
