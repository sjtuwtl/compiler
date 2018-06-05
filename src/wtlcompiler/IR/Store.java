package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;
import wtlcompiler.IR.Value.Register;

import java.util.LinkedList;
import java.util.List;

public class Store extends IRInstruction{
    private IntegerValue address;
    private IntegerValue data;
    private PhysicalRegister dataReg;

    public Store(Label label, IntegerValue address, IntegerValue data) {
        super(label);
        this.address = address;
        this.data = data;
    }

    public IntegerValue getData() {
        return data;
    }

    public PhysicalRegister getDataReg() {
        return dataReg;
    }

    public void setData(IntegerValue data) {
        this.data = data;
    }

    public void setDataReg(PhysicalRegister dataReg) {
        this.dataReg = dataReg;
    }

    public IntegerValue getAddress() {
        return address;
    }

    @Override
    public String toString() {
        if (data == null) return "Store NULL to" + address.toString();
        else if (address == null) return "Store " + data.toString() + " to NULL";
        else return "Store " + data.toString() + " to " + address.toString();

    }

    @Override
    public Register getDefRegister() {
       return (Register) address;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        Address tmp;
        if (data instanceof Address) {
            tmp = (Address) data;
            while (tmp.getBase() != null) {
                if (tmp.getOffset() instanceof Register) usedRegister.add((Register) tmp.getOffset());
                tmp = tmp.getBase();
            }
            usedRegister.add(tmp);
        }
        else if (data instanceof Register) usedRegister.add((Register) data);
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
