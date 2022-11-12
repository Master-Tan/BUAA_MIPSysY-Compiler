package backend.Instructions;

public enum MIPSBinaryIType {

    addi,
    subi,
    andi,
    ori;

    @Override
    public String toString() {
        switch (this) {
            case addi: {
                return "addi";
            }
            case subi: {
                return "subi";
            }
            case andi: {
                return "andi";
            }
            case ori: {
                return "ori";
            }
            default: {
                return null;
            }
        }
    }

}
