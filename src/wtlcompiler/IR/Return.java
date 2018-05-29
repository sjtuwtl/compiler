package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;

public class Return extends Terminator{
    private IntegerValue value;
    private PhysicalRegister valueReg;

    public Return(Label label, IntegerValue value) {
        super(label);
        this.value = value;
    }

    public IntegerValue getValue() {
        return value;
    }

    public PhysicalRegister getValueReg() {
        return valueReg;
    }

    public void setValue(IntegerValue value) {
        this.value = value;
    }

    public void setValueReg(PhysicalRegister valueReg) {
        this.valueReg = valueReg;
    }

    @Override
    public String toString()
    {
        return "Return: " + value.toString();
    }

    @Override
    public void accept(IRInstTraversal visitor)
    {
        visitor.visit(this);
    }
}
