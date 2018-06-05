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
    public List<Register> getDefRegister() {
        List<Register> tmp = new LinkedList<>();
        if (dest instanceof Address) {
            if (((Address) dest).getBase() != null) {
                tmp.add((Register) ((Address) dest).getBase());
                if (((Address) dest).getOffset() instanceof Register)
                    tmp.add((Register) ((Address) dest).getOffset());
                ((Address) dest).getBase().setUsedRegister();
                tmp.addAll(((Address) dest).getBase().usedRegister);
                if (((Address) dest).getOffset() instanceof Address){
                    ((Address) dest).getOffset().setUsedRegister();
                    tmp.addAll(((Address) dest).getOffset().usedRegister);
                }
            }
        }
        else if (dest instanceof Register) tmp.add((Register) dest);
        return tmp;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if (lhs instanceof Address) {
            if (((Address) lhs).getBase() != null) {
                usedRegister.add((Register) ((Address) lhs).getBase());
                if (((Address) lhs).getOffset() instanceof Register)
                    usedRegister.add((Register) ((Address) lhs).getOffset());
                ((Address) lhs).getBase().setUsedRegister();
                usedRegister.addAll(((Address) lhs).getBase().usedRegister);
                if (((Address) lhs).getOffset() instanceof Address){
                    ((Address) lhs).getOffset().setUsedRegister();
                    usedRegister.addAll(((Address) lhs).getOffset().usedRegister);
                }
            }
        }
        else if (lhs instanceof Register) usedRegister.add((Register) lhs);
        if (rhs instanceof Address) {
            if (((Address) rhs).getBase() != null) {
                usedRegister.add((Register) ((Address) rhs).getBase());
                if (((Address) rhs).getOffset() instanceof Register)
                    usedRegister.add((Register) ((Address) rhs).getOffset());
                ((Address) rhs).getBase().setUsedRegister();
                usedRegister.addAll(((Address) rhs).getBase().usedRegister);
                if (((Address) rhs).getOffset() instanceof Address){
                    ((Address) rhs).getOffset().setUsedRegister();
                    usedRegister.addAll(((Address) rhs).getOffset().usedRegister);
                }
            }
        }
        else if (rhs instanceof Register) usedRegister.add((Register) rhs);
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
