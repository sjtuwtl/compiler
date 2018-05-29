package wtlcompiler.IR.Value;

import wtlcompiler.IR.IRType.IRType;
import wtlcompiler.utility.Name;

public class Address extends VitualRegister{
    private Name name;
    private Address base;
    private IntegerValue offset;
    private IRType irType;

    private PhysicalRegister baseReg, offsetReg;

    public Address(Name name, IRType irType) {
        super(name, true);
        this.name = name;
        this.irType = irType;
    }

    public Address(Name name, Address base, IntegerValue offset) {
        super(name);
        this.base = base;
        this.offset = offset;
    }

    public Name getName() {
        return name;
    }

    public Address getBase() {
        return base;
    }

    public IntegerValue getOffset() {
        return offset;
    }

    public IRType getIrType() {
        return irType;
    }

    public void setBaseReg(PhysicalRegister baseReg) {
        this.baseReg = baseReg;
    }

    public void setOffsetReg(PhysicalRegister offsetReg) {
        this.offsetReg = offsetReg;
    }

    public PhysicalRegister getBaseReg() {
        return baseReg;
    }

    public PhysicalRegister getOffsetReg() {
        return offsetReg;
    }

    @Override
    public String toString() {
        if (this.offset == null)
            return "[" + name.toString() + "]";
        else
            return "Address: " + name.toString() + "+ offset" + offset.toString() + "* i64;";
    }
}
