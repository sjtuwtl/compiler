package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;
import wtlcompiler.IR.Value.Register;

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
        if (value == null)
            return "Return";
        else
            return "Return: " + value.toString();
    }

    @Override
    public Register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if (value instanceof Register)
            usedRegister.add((Register) value);
    }

    @Override
    public void accept(IRInstTraversal visitor)
    {
        visitor.visit(this);
    }
}
