package backend.Instructions;

public enum MIPSBinaryType {

    addu,
    subu,
    mulu,
    // 除法特殊，与取余结合
    and,
    or;

    @Override
    public String toString() {
        switch (this) {
            case addu: {
                return "addu";
            }
            case subu: {
                return "subu";
            }
            case mulu: {
                return "mulu";
            }
            case and: {
                return "and";
            }
            case or: {
                return "or";
            }
            default: {
                return null;
            }
        }
    }

}
