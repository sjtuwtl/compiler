package wtlcompiler.IR.Value;

import wtlcompiler.IR.IRInstruction;
import wtlcompiler.IR.IRBase.IRInstTraversal;

import java.util.List;

public class IntegerValue extends IRInstruction{
    public String toString() {
        return "Integer";
    }

    @Override
    public List<Register> getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() { }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
