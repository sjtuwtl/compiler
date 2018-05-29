package wtlcompiler.IR.IRType;

import wtlcompiler.IR.Value.*;

public class Array extends IRType{
    private IRType base;
    private IntegerValue size;

    public Array(IRType base, IntegerValue size) {
        this.base = base;
        this.size = size;
    }

    @Override
    public String toString() {
        if (size != null)
            return "[" + base.toString() + "*" + size.toString() + "]";
        else return "[ArrayType: " + base.toString() + "[] ]";
    }

}
