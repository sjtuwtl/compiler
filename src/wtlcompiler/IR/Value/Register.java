package wtlcompiler.IR.Value;

import wtlcompiler.utility.Name;

public class Register extends IntegerValue {
    private Name name;

    public Register(Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
