package wtlcompiler.IR;

import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;

import java.util.LinkedList;
import java.util.List;

public class Compare extends IRInstruction{
    public enum Condition {
        SLT, SGT, SEQ, BEQ, EQU, NEQ;
    }

    private Condition condition;
    private IntegerValue lhs;
    private IntegerValue rhs;
    private Address dest;
    private PhysicalRegister destReg;
    private PhysicalRegister lhsReg, rhsReg;

    public Compare(Label label, Condition condition, Address dest, IntegerValue lhs, IntegerValue rhs) {
        super(label);
        this.condition = condition;
        this.dest = dest;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Address getDest() {
        return dest;
    }

    public IntegerValue getRhs() {
        return rhs;
    }

    public IntegerValue getLhs() {
        return lhs;
    }

    public PhysicalRegister getDestReg() {
        return destReg;
    }

    public void setDestReg(PhysicalRegister destReg) {
        this.destReg = destReg;
    }

    public PhysicalRegister getLhsReg() {
        return lhsReg;
    }

    public PhysicalRegister getRhsReg() {
        return rhsReg;
    }

    public void setLhsReg(PhysicalRegister lhsReg) {
        this.lhsReg = lhsReg;
    }

    public void setRhsReg(PhysicalRegister rhsReg) {
        this.rhsReg = rhsReg;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        String ret = "";
        if(dest != null)
            ret += dest.toString() + " = ";
        if(lhs != null)
            ret += lhs.toString() + " ";
        ret += condition.toString() + " ";
        if(rhs != null)
            ret += rhs.toString();
        return ret;
        //return dest.toString() + "=" + lhs.toString() + " " + condition.toString() + " " + rhs.toString();
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
    public void setUsedRegister() {
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
