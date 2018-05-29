package wtlcompiler.IR;

import wtlcompiler.utility.Name;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.IRType.IRType;

public class Parameter {
    private IRType irType;
    private Name name;
    private Address address;
    private boolean isAdded;

    public Parameter(IRType irType, Name name, Address address) {
        this.irType = irType;
        this.name = name;
        this.address = address;
        isAdded = false;
    }

    public Parameter(IRType irType, Name name, Address address, boolean isAdded) {
        this.irType = irType;
        this.name = name;
        this.isAdded = isAdded;
        this.address = address;
    }

    public IRType getIrType() {
        return irType;
    }

    public Name getName() {
        return name;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public Address getAddress() {
        return address;
    }
}
