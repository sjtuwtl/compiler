package wtlcompiler.utility;

public enum  UnaryOp {
    POS, NEG, INC, DEC, NOT, BIT_NOT, SUF_INC, SUF_DEC;

    public String toString(UnaryOp op) {
        return op.name();
    }

    public static boolean changeValue(UnaryOp op) {
        return (op == INC || op == DEC || op == SUF_INC || op == SUF_DEC);
    }

    public static boolean isSuffix(UnaryOp op) {
        return (op ==SUF_INC || op == SUF_DEC);
    }
}
