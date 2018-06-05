package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.PhysicalRegister;
import wtlcompiler.IR.Value.Register;
import wtlcompiler.IR.Value.VitualRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class IRInstruction {
    private Label label;
    private IRInstruction next;
    private IRInstruction prev;
    public Set<VitualRegister> liveIn = null;
    public Set<VitualRegister> liveOut = null;
    public List<Register> usedRegister = new LinkedList<>();

    public IRInstruction(Label lab, IRInstruction nex, IRInstruction pre) {
        label = lab;
        next = nex;
        prev = pre;
    }

    public IRInstruction() {}

    public IRInstruction(Label lab) {
        label = lab;
    }

    public Label getLabel() {
        return label;
    }

    public IRInstruction getNext() {
        return next;
    }

    public IRInstruction getPrev() {
        return prev;
    }

    public void setNext(IRInstruction next) {
        this.next = next;
    }

    public void setPrev(IRInstruction prev) {
        this.prev = prev;
    }

    public abstract String toString();

    public abstract void accept(IRInstTraversal visitor);

    public abstract Register getDefRegister();
    //public abstract void setDefRegister(Register reg);
    public abstract void setUsedRegister();
    public List<Register> getUsedRegister(){
        return usedRegister;
    }
    //public abstract void resetUsedRegister(Map<VitualRegister, PhysicalRegister> allocateMap);
}

