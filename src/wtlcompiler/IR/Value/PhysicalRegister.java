package wtlcompiler.IR.Value;

import wtlcompiler.utility.Name;

public class PhysicalRegister extends Register{
    private boolean isCaller;
    private boolean isCallee;

    public PhysicalRegister(Name name) {
        super(name);
    }

    public boolean isCallee() {
        return isCallee;
    }

    public boolean isCaller() {
        return isCaller;
    }

    @Override
    public String toString() {
        return super.getName().toString();
    }
}
