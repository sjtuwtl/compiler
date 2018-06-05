package wtlcompiler.IR;

import wtlcompiler.utility.Name;
import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;

import java.util.LinkedList;
import java.util.List;

public class Call extends IRInstruction{
    private Name functionName;
    private List<IntegerValue> params;
    private Address dest;
    private PhysicalRegister destReg;

    public Call(Label label, Address dest, Name functionName, List<IntegerValue> params) {
        super(label);
        this.dest = dest;
        this.functionName = functionName;
        this.params = params;
    }

    public PhysicalRegister getDestReg() {
        return destReg;
    }

    public void setDestReg(PhysicalRegister destReg) {
        this.destReg = destReg;
    }

    public Name getFunctionName() {
        return functionName;
    }

    public List<IntegerValue> getParams() {
        return params;
    }

    public Address getDest() {
        return dest;
    }

    public String paramToString() {
        String tmp = "(";
        int i = 0;
        for (IntegerValue item : params) {
            if (i != params.size() - 1)
                tmp += item.toString() + ",";
            else
                tmp += item.toString() + ")";
            i++;
        }
        return tmp;
    }

    @Override
    public String toString() {
        String tmp = "";
        if(this.dest != null)
            tmp += dest.toString() + " = ";
        tmp += "Call @" + functionName.toString() + paramToString();
        return tmp;
    }

    @Override
    public List<Register> getDefRegister() {
        List<Register> tmp = new LinkedList<>();
        if (dest instanceof Address) {
            if (((Address) dest).getBase() != null) {
                tmp.add((Register) ((Address) dest).getBase());
                if (((Address) dest).getOffset() instanceof Register)
                    tmp.add((Register) ((Address) dest).getOffset());
                ((Address) dest).getBase().setUsedRegister();
                tmp.addAll(((Address) dest).getBase().usedRegister);
                if (((Address) dest).getOffset() instanceof Address){
                    ((Address) dest).getOffset().setUsedRegister();
                    tmp.addAll(((Address) dest).getOffset().usedRegister);
                }
            }
        }
        else if (dest instanceof Register) tmp.add((Register) dest);
        return tmp;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        for (IntegerValue item : params)
            if (item instanceof Register) {
                if (item instanceof Address) {
                    if (((Address) item).getBase() != null) {
                        usedRegister.add((Register) ((Address) item).getBase());
                        if (((Address) item).getOffset() instanceof Register)
                            usedRegister.add((Register) ((Address) item).getOffset());
                        ((Address) item).getBase().setUsedRegister();
                        usedRegister.addAll(((Address) item).getBase().usedRegister);
                        if (((Address) item).getOffset() instanceof Address){
                            ((Address) item).getOffset().setUsedRegister();
                            usedRegister.addAll(((Address) item).getOffset().usedRegister);
                        }
                    }
                }
                else usedRegister.add((Register) item);
            }
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
