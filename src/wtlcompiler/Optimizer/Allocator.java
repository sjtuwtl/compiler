package wtlcompiler.Optimizer;

import wtlcompiler.IR.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.IR.Value.*;
import wtlcompiler.utility.Name;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Allocator extends  RegisterAllocator implements IRInstTraversal {
    private IRInstruction entry;
    private IRInstruction initializeEntry;
    private Map<VitualRegister, PhysicalRegister> registerMap = new HashMap<>();
    private List<PhysicalRegister> physicalRegisters;
    private String[] registerNames = {"rax", "rcx", "rdx", "rbx",
            "r8",  "r9", "r10", "r11", "r12", "r13", "r14", "r15"};
    private String[] parameterRegNames = {"rdi", "rsi", "rdx", "rcx", "r8", "r9"};
    private boolean[] isAvailable;

    public Allocator(IRInstruction entry, IRInstruction initializeEntry) {
        this.entry = entry;
        this.initializeEntry = initializeEntry;
        physicalRegisters = new ArrayList<>(16);
        isAvailable = new boolean[16];
        initRegisterList();
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
    public void visit(IRInstruction inst)
    {
        resetAvailable();
        registerMap.clear();
        inst.accept(this);
    }

    @Override
    public void visit(Alloca inst) {
    }

    @Override
    public void visit(Malloc inst)
    {
        inst.setReturnReg(physicalRegisters.get(0));
        isAvailable[0] = false;
        if(inst.getSize() instanceof VitualRegister)
            inst.setSizeReg(getPhysicalRegister((VitualRegister) inst.getSize()));
    }

    @Override
    public void visit(BinaryOp inst)
    {
        //const value folded
        if(inst.getOp() == BinaryOp.BinOp.idiv)
        {
            inst.setDestReg(physicalRegisters.get(0));
            isAvailable[0] = false;
        }
        else if(inst.getOp() == BinaryOp.BinOp.mod)
        {
            inst.setOp(BinaryOp.BinOp.idiv);
            inst.setDestReg(physicalRegisters.get(2));
            isAvailable[2] = false;
        }
        else
        {
            PhysicalRegister destPr = getPhysicalRegister(inst.getDest());
            inst.setDestReg(destPr);
        }

        if(inst.getLhs() instanceof VitualRegister)
        {
            PhysicalRegister lhsPr;
            if(inst.getOp() == BinaryOp.BinOp.idiv)
            {
                lhsPr = physicalRegisters.get(0);
                isAvailable[0] = false;
            }
            else
                lhsPr = getPhysicalRegister((VitualRegister) inst.getLhs());
            inst.setLhsReg(lhsPr);
        }
        if(inst.getRhs() instanceof VitualRegister)
        {
            PhysicalRegister rhsPr = getPhysicalRegister((VitualRegister) inst.getRhs());
            inst.setRhsReg(rhsPr);
        }
        else
        {
            PhysicalRegister rhsPr = getAvailablePhysicalRegister();
            inst.setRhsReg(rhsPr);
        }
    }

    @Override
    public void visit(Branch inst)
    {

    }

    @Override
    public void visit(Call inst)
    {
        for(IntegerValue parameter : inst.getParams())
        {
            resetAvailable();
            if(parameter instanceof VitualRegister)
                allocRegisterForAddress((VitualRegister) parameter);
        }
        if(inst.getDest() != null)
        {
            PhysicalRegister destPr = physicalRegisters.get(0);
            inst.setDestReg(destPr);
            isAvailable[0] = false;
        }
    }

    @Override
    public void visit(Compare inst)
    {
        PhysicalRegister destPr = getPhysicalRegister(inst.getDest());
        inst.setDestReg(destPr);
        if(inst.getLhs() instanceof VitualRegister)
        {
            PhysicalRegister lhsPr = getPhysicalRegister((VitualRegister) inst.getLhs());
//            PhysicalRegister lhsPr = getAvailablePhysicalRegister();
            inst.setLhsReg(lhsPr);
        }
        else if(inst.getLhs() instanceof Immediate)
        {
            PhysicalRegister lhsPr = getAvailablePhysicalRegister();
            inst.setLhsReg(lhsPr);
        }
        if(inst.getRhs() instanceof VitualRegister)
        {
            PhysicalRegister rhsPr = getPhysicalRegister((VitualRegister) inst.getRhs());
            inst.setRhsReg(rhsPr);
        }
    }

    @Override
    public void visit(Function inst)
    {

    }

    @Override
    public void visit(Jump inst)
    {

    }

    @Override
    public void visit(Label inst)
    {

    }

    @Override
    public void visit(MemCopy inst)
    {
        inst.setDataReg(getPhysicalRegister(inst.getFromAddress()));
    }

    @Override
    public void visit(Move inst)
    {
        //TODO
        //because we do not have move instructions now
    }

    @Override
    public void visit(Return inst)
    {
        if(inst.getValue() instanceof VitualRegister)
        {
            PhysicalRegister pr = getPhysicalRegister((VitualRegister) inst.getValue());
            inst.setValueReg(pr);
        }
    }

    @Override
    public void visit(Store inst)
    {
        if(inst.getAddress() instanceof VitualRegister)
            allocRegisterForAddress((VitualRegister) inst.getAddress());
        if(inst.getData() instanceof VitualRegister)
        {
            PhysicalRegister pr = getPhysicalRegister((VitualRegister) inst.getData());
            inst.setDataReg(pr);
        }
    }

    private void initRegisterList()
    {
        //R0  R1  R2  R3  R4  R5  R6  R7  R8  R9  R10  R11  R12  R13  R14  R15
        //rax rcx rdx rbx rsp rbp rsi rdi
        for(String name : registerNames)
        {
            physicalRegisters.add(new PhysicalRegister(Name.getName(name)));
        }
        resetAvailable();

    }

    private void resetAvailable()
    {
        for(int i = 0; i < 16; ++i)
            isAvailable[i] = true;
    }


    private void allocRegisterForAddress(VitualRegister vr)
    {
        if(vr instanceof Address)
        {
            if(((Address) vr).getBase() != null)
            {
                PhysicalRegister basePr = getAvailablePhysicalRegister();
                ((Address) vr).setBaseReg(basePr);
                if (!(((Address) vr).getOffset() instanceof Immediate))
                {
                    PhysicalRegister offsetPr = getAvailablePhysicalRegister();
                    ((Address) vr).setOffsetReg(offsetPr);
                }
                allocRegisterForAddress(((Address) vr).getBase());
                if(((Address) vr).getOffset() instanceof Address)
                    allocRegisterForAddress((Address)((Address) vr).getOffset());
            }
        }
    }
    private PhysicalRegister getPhysicalRegister(VitualRegister vr)
    {
        allocRegisterForAddress(vr);
        if(registerMap.containsKey(vr))
            return registerMap.get(vr);
        else
        {
            PhysicalRegister pr = getAvailablePhysicalRegister();
            registerMap.put(vr, pr);
            return pr;
        }
    }

    private PhysicalRegister getAvailablePhysicalRegister()
    {
        int i = 0;
        while(true)
        {
            if(!isAvailable[i])
                ++i;
            else break;
        }
        isAvailable[i] = false;
        return physicalRegisters.get(i);
    }
}

