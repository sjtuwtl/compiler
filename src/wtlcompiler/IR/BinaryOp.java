package wtlcompiler.IR;

import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;

import wtlcompiler.IR.Value.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BinaryOp extends IRInstruction{
    public enum BinOp {
        add, sub, imul, idiv, mod, sal, sar, and, or, xor, neg, not
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
    public Register getDefRegister() {
        Address tmp = dest;
        if (tmp != null)
            while (tmp.getBase() != null)
                tmp = tmp.getBase();
        return tmp;
    }

    @Override
    public void setUsedRegister(){
        usedRegister.clear();
        Address tmp = dest;
        while (tmp.getBase() != null) {
            if (tmp.getOffset() instanceof Register) usedRegister.add((Register) tmp.getOffset());
            tmp = tmp.getBase();
        }

        if (lhs instanceof Address) {
            tmp = (Address) lhs;
            while (tmp.getBase() != null) {
                if (tmp.getOffset() instanceof Register) usedRegister.add((Register) tmp.getOffset());
                tmp = tmp.getBase();
            }
            usedRegister.add(tmp);
        }
        else if (lhs instanceof Register) usedRegister.add((Register) lhs);

        if (rhs instanceof Address) {
            tmp = (Address) rhs;
            while (tmp.getBase() != null) {
                if (tmp.getOffset() instanceof Register) usedRegister.add((Register) tmp.getOffset());
                tmp = tmp.getBase();
            }
            usedRegister.add(tmp);
        }
        else if (rhs instanceof Register) usedRegister.add((Register) rhs);

    }


    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
