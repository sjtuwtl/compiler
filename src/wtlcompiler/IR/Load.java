package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.*;

import java.util.List;

public class Load extends IRInstruction {
    private VitualRegister dest;
    private Address address;
    private Immediate value;
    private PhysicalRegister destReg;
    private PhysicalRegister sourReg;

    public Load(Label label, VitualRegister dest, Address address) {
        super(label);
        this.dest = dest;
        this.address = address;
    }

    public Load(Label label, VitualRegister dest, Immediate value) {
        super(label);
        this.dest = dest;
        this.value = value;
    }

    public PhysicalRegister getSourReg() {
        return sourReg;
    }

    public PhysicalRegister getDestReg() {
        return destReg;
    }

    public void setSourReg(PhysicalRegister sourReg) {
        this.sourReg = sourReg;
    }

    public void setDestReg(PhysicalRegister destReg) {
        this.destReg = destReg;
    }

    public VitualRegister getDest() {
        return dest;
    }

    public Address getAddress() {
        return address;
    }

    public Immediate getValue() {
        return value;
    }

    @Override
    public String toString() {
        if(this.address != null)
            return dest.toString() + " = Load " + address.toString();
        else return dest.toString() + " = Load " + value.toString();
    }

    @Override
    public List<Register> getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() { }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
