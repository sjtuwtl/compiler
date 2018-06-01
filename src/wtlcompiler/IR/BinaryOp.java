package wtlcompiler.IR;

import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;

import wtlcompiler.IR.Value.*;
public class BinaryOp extends IRInstruction{
    public enum BinOp {
        add, sub, imul, idiv, mod, shl, shr, and, or, xor, neg, not
    }

    private BinOp op;
    private Address dest;
    private IntegerValue lhs;
    private IntegerValue rhs;

    private PhysicalRegister destReg;
    private PhysicalRegister lhsReg, rhsReg;

    public BinaryOp(Label label, BinOp op, Address reg, IntegerValue left, IntegerValue right) {
        super(label);
        this.op = op;
        dest = reg;
        lhs = left;
        rhs = right;
    }

    public PhysicalRegister getDestReg() {
        return destReg;
    }

    public void setDestReg(PhysicalRegister destReg) {
        this.destReg = destReg;
    }

    public void setRhsReg(PhysicalRegister rhsReg) {
        this.rhsReg = rhsReg;
    }

    public void setLhsReg(PhysicalRegister lhsReg) {
        this.lhsReg = lhsReg;
    }

    public PhysicalRegister getRhsReg() {
        return rhsReg;
    }

    public PhysicalRegister getLhsReg() {
        return lhsReg;
    }

    public BinOp getOp() {
        return op;
    }

    public void setOp(BinOp op) {
        this.op = op;
    }

    public Address getDest() {
        return dest;
    }

    public IntegerValue getLhs() {
        return lhs;
    }

    public IntegerValue getRhs() {
        return rhs;
    }

    @Override
    public String toString() {
        String tmp = dest.toString() + " = " + lhs.toString() + " " + op.toString();
        if (rhs != null)
            tmp += " " + rhs.toString();
        return tmp;
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
