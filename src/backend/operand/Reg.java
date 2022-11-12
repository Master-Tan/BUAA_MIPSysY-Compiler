package backend.operand;

public class Reg extends MipsOperand {

    private int regNumber;

    public Reg(int regNumber) {
        this.regNumber = regNumber;
    }

    // 把寄存器编号转为名字
    public String regIdToName(int id) {
        String name;
        if (id == 0) {
            name = "zero";
        } else if (id == 1) {
            name = "at";
        } else if (id >= 2 && id <= 3) {
            name = "v" + (id - 2);
        } else if (id >= 4 && id <= 7) {
            name = "a" + (id - 4);
        } else if (id >= 8 && id <= 15) {
            name = "t" + (id - 8);
        } else if (id >= 16 && id <= 23) {
            name = "s" + (id - 16);
        } else if (id >= 24 && id <= 25) {
            name = "t" + (id - 24 + 8);
        } else if (id >= 26 && id <= 27) {
            name = "k" + (id - 26);
        } else if (id == 28) {
            name = "gp";
        } else if (id == 29) {
            name = "sp";
        } else if (id == 30) {
            name = "fp";
        } else {
            name = "ra";
        }
        return name;
    }

    @Override
    public String toString() {
        return "$" + this.regIdToName(this.regNumber);
    }

}
