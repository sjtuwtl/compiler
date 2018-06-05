package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Register;

public class Move extends IRInstruction{
    private Register dest;
    private Register data;

    public Move(Label label, Register dest, Register data) {
        super(label);
        this.data = data;
        this.dest = dest;
    }

    @Override
    public String toString() {
        return dest.toString() + " = Move " + data.toString();
    }

    @Override
    public Register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() { }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
