package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;

import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRType.*;

public class Alloca extends IRInstruction{
    private Address dest;
    private IntegerValue size;
    private IRType type;

    public Alloca(Label label, Address dest, IntegerValue size) {
        super(label);
        this.dest = dest;
        this.size = size;
    }

    public Alloca(Label label, Address dest, IRType type) {
        super(label);
        this.dest = dest;
        this.type = type;
    }

    public Address getDest() {
        return dest;
    }

    @Override
    public String toString() {
        if (this.size != null)
            return dest.toString() + "= alloca  " + size.toString();
        else
            return dest.toString() + "= alloca  " + type.toString();
    }

    @Override
    public void accept(IRInstTraversal visitor)
    {
        visitor.visit(this);
    }
}
