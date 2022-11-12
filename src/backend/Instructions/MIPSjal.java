package backend.Instructions;

import backend.operand.Label;

public class MIPSjal extends MipsInstruction {

    private Label label;

    public MIPSjal(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "jal " + label.toString();
    }

}
