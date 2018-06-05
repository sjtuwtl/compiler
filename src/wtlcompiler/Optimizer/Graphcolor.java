package wtlcompiler.Optimizer;

import wtlcompiler.IR.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.*;
import wtlcompiler.utility.Name;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graphcolor implements IRInstTraversal {
    private Map<VitualRegister, Integer> map;
    private IRInstruction entry;
    private IRInstruction initializeEntry;
    private String[] registerNames = {"rcx", "rbx", "rsi", "rdi",
            "r8",  "r9", "r10", "r11", "r12", "r13", "r14", "r15"};
    private PhysicalRegister number0 = new PhysicalRegister(Name.getName("rax"));
    private PhysicalRegister number2 = new PhysicalRegister(Name.getName("rdx"));
    private PhysicalRegister numbertmp = new PhysicalRegister(Name.getName("r15"));
    private List<PhysicalRegister> physicalRegisters = new LinkedList<>();

    public Graphcolor(Map<VitualRegister, Integer> map, IRInstruction entry, IRInstruction initializeEntry) {
        this.map = map;
        this.entry = entry;
        this.initializeEntry = initializeEntry;
        for (String name : registerNames)
            physicalRegisters.add(new PhysicalRegister(Name.getName(name)));
    }

    public void process() {
        IRInstruction cur = entry;
        while(cur != null) {
            visit(cur);
            cur = cur.getNext();
        }
        IRInstruction curr = initializeEntry;
        while(curr != null) {
            visit(curr);
            curr = curr.getNext();
        }
    }

    @Override
    public void visit(IRInstruction inst) {
        inst.accept(this);
    }

    @Override
    public void visit(Alloca inst) { }

    @Override
    public void visit(Malloc inst) {
        inst.setReturnReg(number0);
        if(inst.getSize() instanceof VitualRegister)
            inst.setSizeReg(getPhysicalRegister((VitualRegister) inst.getSize()));
    }

    @Override
    public void visit(BinaryOp inst) {
        if(inst.getOp() == BinaryOp.BinOp.idiv || inst.getOp() == BinaryOp.BinOp.mod) {
            visitDiv(inst);
            return;
        }
        PhysicalRegister destPr = getPhysicalRegister(inst.getDest());
        inst.setDestReg(destPr);

        if(inst.getLhs() instanceof VitualRegister) {
            PhysicalRegister lhsPr = getPhysicalRegister((VitualRegister) inst.getLhs());
            inst.setLhsReg(lhsPr);
        }
        if(inst.getRhs() instanceof VitualRegister) {
            PhysicalRegister rhsPr = getPhysicalRegister((VitualRegister) inst.getRhs());
            inst.setRhsReg(rhsPr);
        }
        else if (!(inst.getRhs() instanceof Immediate))
            inst.setRhsReg(physicalRegisters.get
                    (map.get
                            (inst.getRhs())));

    }

    public void visitDiv(BinaryOp inst) {
        if(inst.getOp() == BinaryOp.BinOp.idiv) {
            inst.setDestReg(number0);
        }
        else {
            inst.setOp(BinaryOp.BinOp.idiv);
            inst.setDestReg(number2);
        }
        if(inst.getLhs() instanceof VitualRegister)
            allocRegisterForAddress((VitualRegister) inst.getLhs());
        inst.setLhsReg(number0);
        if(inst.getRhs() instanceof VitualRegister) {
            PhysicalRegister rhsPr = getPhysicalRegister((VitualRegister) inst.getRhs());
            inst.setRhsReg(rhsPr);
        }
        else if (!(inst.getRhs() instanceof Immediate))
        inst.setRhsReg(physicalRegisters.get
                (map.get
                        (inst.getRhs())));
    }

    @Override
    public void visit(Branch inst) { }

    @Override
    public void visit(Call inst) {
        for(IntegerValue parameter : inst.getParams()) {
            if(parameter instanceof VitualRegister)
                allocRegisterForAddress((VitualRegister) parameter);
        }
        if(inst.getDest() != null)
            inst.setDestReg(number0);
    }

    @Override
    public void visit(Compare inst) {
        PhysicalRegister destPr = getPhysicalRegister(inst.getDest());
        inst.setDestReg(destPr);
        if(inst.getLhs() instanceof VitualRegister) {
            PhysicalRegister lhsPr = getPhysicalRegister((VitualRegister) inst.getLhs());
            inst.setLhsReg(lhsPr);
        }
        else if(inst.getLhs() instanceof Immediate)
            inst.setLhsReg(physicalRegisters.get(map.get(inst.getLhs())));
        if(inst.getRhs() instanceof VitualRegister) {
            PhysicalRegister rhsPr = getPhysicalRegister((VitualRegister) inst.getRhs());
            inst.setRhsReg(rhsPr);
        }
    }

    @Override
    public void visit(Function inst) { }

    @Override
    public void visit(Jump inst) {
    }

    @Override
    public void visit(Label inst) { }

    @Override
    public void visit(MemCopy inst) {
        inst.setDataReg(getPhysicalRegister(inst.getFromAddress()));
    }

    @Override
    public void visit(Move inst) { }

    @Override
    public void visit(Return inst) {
        if(inst.getValue() instanceof VitualRegister) {
            PhysicalRegister pr = getPhysicalRegister((VitualRegister) inst.getValue());
            inst.setValueReg(pr);
        }
    }

    @Override
    public void visit(Store inst) {
        if(inst.getAddress() instanceof VitualRegister)
            allocRegisterForAddress((VitualRegister) inst.getAddress());
        if(inst.getData() instanceof VitualRegister) {
            PhysicalRegister pr = getPhysicalRegister((VitualRegister) inst.getData());
            inst.setDataReg(pr);
        }
    }

    private void allocRegisterForAddress(VitualRegister vr) {
        if(vr instanceof Address) {
            if(((Address) vr).getBase() != null) {
                ((Address) vr).setBaseReg(physicalRegisters.get(map.get(((Address) vr).getBase())));
                if (!(((Address) vr).getOffset() instanceof Immediate))
                    ((Address) vr).setOffsetReg(physicalRegisters.get(map.get(((Address) vr).getOffset())));
                allocRegisterForAddress(((Address) vr).getBase());
                if(((Address) vr).getOffset() instanceof Address)
                    allocRegisterForAddress((Address)((Address) vr).getOffset());
            }
        }
    }

    private PhysicalRegister getPhysicalRegister(VitualRegister vr) {
        allocRegisterForAddress(vr);
        if (map.get(vr) != null)
            return physicalRegisters.get(map.get(vr));
        else
            return numbertmp;
    }
}
