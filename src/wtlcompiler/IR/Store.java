package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;

public class Store extends IRInstruction{
    private IntegerValue address;
    private IntegerValue data;
    private PhysicalRegister dataReg;

    public Store(Label label, IntegerValue address, IntegerValue data) {
        super(label);
        this.address = address;
        this.data = data;
    }

    public IntegerValue getData() {
        return data;
    }

    public PhysicalRegister getDataReg() {
        return dataReg;
    }

    public void setData(IntegerValue data) {
        this.data = data;
    }

    public void setDataReg(PhysicalRegister dataReg) {
        this.dataReg = dataReg;
    }

    @Override
    public String toString() {
        if (data == null) return "Store " + address.toString();
        else if (address == null) return "Store " + data.toString();
        else return "Store " + data.toString() + " to " + address.toString();

    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
