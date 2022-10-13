package grammaticalAnalysis.grammatical;

import grammaticalAnalysis.SymbolTable;
import grammaticalAnalysis.grammatical.Stmt.StmtReturnNode;

import java.util.ArrayList;

public class BlockNode extends Node {

    private ArrayList<BlockItemNode> blockItemNodes;
    private int endLine;
    private SymbolTable symbolTable;

    public BlockNode(ArrayList<BlockItemNode> blockItemNodes, int endLine, int line, SymbolTable symbolTable) {
        super(line);
        this.endLine = endLine;
        this.blockItemNodes = blockItemNodes;
        this.symbolTable = symbolTable;
    }

    public DataType getReturnType(SymbolTable currentSymbolTable) {
        if (blockItemNodes.size() == 0) {
            return null;
        }
        int i = blockItemNodes.size() - 1;
        if (blockItemNodes.get(i).getStmtNode() != null) {
            if (blockItemNodes.get(i).getStmtNode() instanceof StmtReturnNode) {
                if (((StmtReturnNode) blockItemNodes.get(i).getStmtNode()).getExpNode() == null) {
                    return null;
                } else {
                    return ((StmtReturnNode) blockItemNodes.get(i).getStmtNode()).getExpNode().getDataType(this.symbolTable);
                }
            }
        }
        return null;
    }

    public int getEndLine() {
        return endLine;
    }

}
