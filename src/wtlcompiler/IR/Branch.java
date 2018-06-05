package wtlcompiler.IR;

import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;

import java.util.List;

public class Branch extends Terminator{
    private Label ifTure;
    private Label ifFalse;
    private IntegerValue condition;
    private PhysicalRegister conditionReg;
    private Compare.Condition op;

    public Branch(Label label, Label ifTrue, Label ifFalse, IntegerValue condition) {
        super(label);
        this.condition = condition;
        this.ifFalse = ifFalse;
        this.ifTure = ifTrue;
    }

    public Branch(Label label, Label ifTrue, Label ifFalse, IntegerValue condition, Compare.Condition op) {
        super(label);
        this.condition = condition;
        this.ifFalse = ifFalse;
        this.ifTure = ifTrue;
        this.op = op;
    }

    public Compare.Condition getOp() {
        return op;
    }

    public PhysicalRegister getConditionReg() {
        return conditionReg;
    }

    public void setConditionReg(PhysicalRegister conditionReg) {
        this.conditionReg = conditionReg;
    }

    public Label getIfFalse() {
        return ifFalse;
    }

    public Label getIfTure() {
        return ifTure;
    }

    public IntegerValue getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        String tmp = "Branch: ";
        if (condition != null)
            tmp += condition.toString();
        tmp += " true: " + ifTure.toString() + " false " + ifFalse.toString();
        return tmp;
    }

    @Override
    public Register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() { }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
