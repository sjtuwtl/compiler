package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;

public class Malloc extends IRInstruction{
    private IntegerValue size;
    private Address returnAddress;
    private PhysicalRegister sizeReg;
    private PhysicalRegister returnReg;

    public Malloc(Label label, IntegerValue size, Address returnAddress) {
        super(label);
        this.size = size;
        this.returnAddress = returnAddress;
    }

    public IntegerValue getSize() {
        return size;
    }

    public PhysicalRegister getSizeReg() {
        return sizeReg;
    }

    public PhysicalRegister getReturnReg() {
        return returnReg;
    }

    public void setSizeReg(PhysicalRegister sizeReg) {
        this.sizeReg = sizeReg;
    }

    public void setReturnReg(PhysicalRegister returnReg) {
        this.returnReg = returnReg;
    }

    public Address getReturnAddress() {
        return returnAddress;
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return returnAddress.toString() + "= Malloc " + size.toString();
    }
}
