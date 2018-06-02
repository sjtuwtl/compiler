package wtlcompiler.IR.Value;

import wtlcompiler.IR.IRType.IRType;
import wtlcompiler.utility.Name;

public class Address extends VitualRegister{
    private Name name;
    private Address base;
    private IntegerValue offset;
    private IRType irType;
    private boolean isGlobal = false;
    private boolean isPointer = false;
    private boolean isMember = false;
    private int memberNumber = -1;

    private PhysicalRegister baseReg, offsetReg;

    public Address(Name name, IRType irType) {
        super(name, true);
        this.name = name;
        this.irType = irType;
        this.isGlobal = false;
    }

    public Address(Name name, boolean isGlobal) {
        super(name);
        this.name = name;
        this.isGlobal = isGlobal;
    }

    public Address(Name name, Address base, IntegerValue offset) {
        super(name);
        this.name = name;
        this.base = base;
        this.offset = offset;
        this.isGlobal = false;
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

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public boolean isPointer() {
        return isPointer;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public int getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    public void setPointer(boolean pointer) {
        isPointer = pointer;
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
        if (name == null) return "NoAddress";
        if (this.offset == null)
            return "[" + name.toString() + "]";
        else
            return "[Address: " + name.toString() + ":" + base.toString() + "+ offset " + offset.toString() + "*i64]";
    }
}
