package wtlcompiler.IR.Value;

import wtlcompiler.IR.IRInstruction;
import wtlcompiler.IR.IRBase.IRInstTraversal;

public class IntegerValue extends IRInstruction{
    public String toString() {
        return "Integer";
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
