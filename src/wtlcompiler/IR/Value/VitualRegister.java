package wtlcompiler.IR.Value;

import wtlcompiler.utility.Name;


public class VitualRegister extends Register{
    private int value;
    private boolean isAddress;

    public VitualRegister(Name name, boolean isAddress) {
        super(name);
        this.isAddress = isAddress;
    }

    public VitualRegister(Name name) {
        super(name);
    }

    public int getValue() {
        return value;
    }

    @Override
    public Name getName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
