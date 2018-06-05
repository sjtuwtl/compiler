package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;
import wtlcompiler.IR.Value.Register;

import java.util.LinkedList;
import java.util.List;

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
    public List<Register> getDefRegister() {
        List<Register> tmp = new LinkedList<>();
        tmp.add(returnAddress);
        return tmp;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if (size instanceof Address) {
            if (((Address) size).getBase() != null) {
                usedRegister.add((Register) ((Address) size).getBase());
                if (((Address) size).getOffset() instanceof Register)
                    usedRegister.add((Register) ((Address) size).getOffset());
                ((Address) size).getBase().setUsedRegister();
                usedRegister.addAll(((Address) size).getBase().usedRegister);
                if (((Address) size).getOffset() instanceof Address){
                    ((Address) size).getOffset().setUsedRegister();
                    usedRegister.addAll(((Address) size).getOffset().usedRegister);
                }
            }
        }
        else if (size instanceof Register) usedRegister.add((Register) size);
    }

    @Override
    public String toString() {
        return returnAddress.toString() + "= Malloc " + size.toString();
    }
}
