package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.IRType.IRType;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.Register;
import wtlcompiler.IR.Value.VitualRegister;

import java.util.List;

public class GetElementPtr extends IRInstruction
{
    private VitualRegister dest;
    private IRType type;
    private Address baseAddress;
    private IntegerValue pos1;
    private IntegerValue pos2;

    public GetElementPtr(Label label, VitualRegister dest, IRType type, Address baseAddress, IntegerValue pos1, IntegerValue pos2)
    {
        super(label);
        this.dest = dest;
        this.type = type;
        this.baseAddress = baseAddress;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    public String toString()
    {
        return dest.toString() + " = " + "getElementPtr %" +
                type.toString() + " % " + baseAddress.toString() + " " +
                String.valueOf(pos1) + " " + String.valueOf(pos2);
    }

    @Override
    public List<Register> getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() { }

    @Override
    public void accept(IRInstTraversal visitor)
    {
        throw new RuntimeException("can't reach here");
    }
}
