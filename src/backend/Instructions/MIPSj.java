package backend.Instructions;

import backend.Parser.MipsBasicBlock;
import backend.operand.Label;

public class MIPSj extends MipsInstruction {

//    private MipsBasicBlock target;
    private Label label;

    public MIPSj(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "j " + label.toString();
    }
}
