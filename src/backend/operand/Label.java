package backend.operand;

public class Label extends MipsOperand {

    private String label;

    public Label(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
