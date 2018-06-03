package wtlcompiler.Translator;

import wtlcompiler.IR.*;
import wtlcompiler.IR.IRBase.DataSection;
import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.Immediate;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.PhysicalRegister;
import wtlcompiler.utility.Name;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Translator implements IRInstTraversal {
    private static final int ARGUMENT = 6;
    private boolean isInitialzeInst = false;
    private IRInstruction entry;
    private IRInstruction initializeEntry;
    private DataSection dataSection;
    private DataSection dataZone;
    private DataSection bssZone;
    private Map<Address, StackSlot> addressStackSlotMap;
    private int rspPosition;
    private List<NasmInst> nasmInsts;
    private List<NasmInst> dataInsts;
    private List<NasmInst> dataZoneInsts;
    private List<NasmInst> bssZoneInsts;
    private List<NasmInst> initializeInsts;
    private Function curFunc;
    private String[] parameterRegName = {"rdi", "rsi", "rdx", "rcx", "r8", "r9"};
    private String[] spareRegName = {"rax", "rbx", "r10", "r11",
            "r12", "r13", "r14", "r15"};

    public Translator(IRInstruction entry, IRInstruction initializeEntry, DataSection dataSection, DataSection dataZone, DataSection bssZone) {
        this.entry = entry;
        this.initializeEntry = initializeEntry;
        this.dataSection = dataSection;
        this.dataZone = dataZone;
        this.bssZone = bssZone;
        rspPosition = 8;//cannot begin with 0
        nasmInsts = new LinkedList<>();
        dataInsts = new LinkedList<>();
        dataZoneInsts = new LinkedList<>();
        bssZoneInsts = new LinkedList<>();
        initializeInsts = new LinkedList<>();
        addressStackSlotMap = new HashMap<>();
    }

    public void process() {
        processDataSection();
        processDataZone();
        processBssZone();
        processInitialInst();
        IRInstruction cur = entry;
        while(cur != null) {
            if (cur instanceof Function) {
                addressStackSlotMap.clear();
                rspPosition = 8;
            }
            visit(cur);
            if (cur instanceof Function && ((Function) cur).getName() == Name.getName("main")) {
                for (NasmInst inst : initializeInsts)
                    nasmInsts.add(inst);
            }
            cur = cur.getNext();
        }
        removeRedundantInst();
    }

    private void processDataSection() {
        for(DataSection.DataPiece piece : dataSection.getDataPieces()) {
            addDataInst(dataInsts, NasmInst.Instruction.dq, String.valueOf(piece.getLength()), null);
            addDataInst(dataInsts, NasmInst.Instruction.NULL, piece.getName().toString(), null);
            addDataInst(dataInsts, NasmInst.Instruction.db, stringToAscii(piece.getStrValue()), null);
        }
    }

    private void processDataZone() {
        for(DataSection.DataPiece piece : dataZone.getDataPieces()) {
            addDataInst(dataZoneInsts, NasmInst.Instruction.NULL, piece.getName().toString(), null);
            addDataInst(dataZoneInsts, NasmInst.Instruction.dq, piece.getOtherValue(), null);
        }
    }

    private void processBssZone() {
        for(DataSection.DataPiece piece: bssZone.getDataPieces()) {
            addDataInst(bssZoneInsts, NasmInst.Instruction.NULL, piece.getName().toString(), null);
            addDataInst(bssZoneInsts, NasmInst.Instruction.resq, "1", null);
        }
    }

    private void processInitialInst() {
        IRInstruction cur = initializeEntry;
        isInitialzeInst = true;
        while(cur != null) {
            visit(cur);
            cur = cur.getNext();
        }
        isInitialzeInst = false;
    }

    public List<NasmInst> getNasmInsts() {
        return nasmInsts;
    }

    public List<NasmInst> getDataInsts() {
        return dataInsts;
    }

    public List<NasmInst> getDataZoneInsts() {
        return dataZoneInsts;
    }

    public List<NasmInst> getBssZoneInsts() {
        return bssZoneInsts;
    }

    @Override
    public void visit(IRInstruction inst) {
        inst.accept(this);
    }

    @Override
    public void visit(Alloca inst) {
        mapAddressToSlot(inst.getDest());
    }

    @Override
    public void visit(BinaryOp inst) {
        if (inst.getOp() == BinaryOp.BinOp.idiv || inst.getOp() == BinaryOp.BinOp.mod) {
            visitDivBinaryOp(inst);
            return;
        }
        String left = processIntegerValue(inst.getLhs(), null);
        addInst(NasmInst.Instruction.mov, inst.getDestReg().toString(), left);
        String right = null;
        if (inst.getRhs() != null)
            right = processIntegerValue(inst.getRhs(), inst.getRhsReg());
        if (inst.getOp() == BinaryOp.BinOp.sal || inst.getOp() == BinaryOp.BinOp.sar) {
            addInst(NasmInst.Instruction.mov, "rcx", right);
            right = "cl";
        }
        addInst(NasmInst.Instruction.valueOf(inst.getOp().toString()), inst.getDestReg().toString(), right);
        addInst(NasmInst.Instruction.mov, processAddress(inst.getDest(), null), inst.getDestReg().toString());
    }

    @Override
    public void visit(Branch inst) {
        if(inst.getCondition() == null) {
            addInst(NasmInst.Instruction.jmp, inst.getIfTure().toString(), null);
            return;
        }
        if(inst.getOp() != null) {
            switch (inst.getOp()) {
                case BEQ:
                    addInst(NasmInst.Instruction.jl, inst.getIfFalse().toString(), null);
                    break;
                case EQU:
                    addInst(NasmInst.Instruction.jne, inst.getIfFalse().toString(), null);
                    break;
                case NEQ:
                    addInst(NasmInst.Instruction.je, inst.getIfFalse().toString(), null);
                    break;
                case SEQ:
                    addInst(NasmInst.Instruction.jg, inst.getIfFalse().toString(), null);
                    break;
                case SGT:
                    addInst(NasmInst.Instruction.jle, inst.getIfFalse().toString(), null);
                    break;
                case SLT:
                    addInst(NasmInst.Instruction.jge, inst.getIfFalse().toString(), null);
                    break;
            }
        }
        else
            addInst(NasmInst.Instruction.jz, inst.getIfFalse().toString(), null);
    }

    @Override
    public void visit(Call inst) {
        int i = 0;
        int rspMove = 0;
        for(IntegerValue value : inst.getParams()) {
            String argument = processIntegerValue(value, new PhysicalRegister(Name.getName("rax")));
            if(i < ARGUMENT)
                addInst(NasmInst.Instruction.mov, parameterRegName[i], argument);
            else {
                addInst(NasmInst.Instruction.mov, spareRegName[0], argument);
                addInst(NasmInst.Instruction.push, spareRegName[0], null);
                rspMove += 8;
                rspPosition -= 8;
            }
            ++i;
        }
        addInst(NasmInst.Instruction.call, inst.getFunctionName().toString(), null);
        if(rspMove != 0)
            addInst(NasmInst.Instruction.add, "rsp", String.valueOf(rspMove));
        if(inst.getDest() != null)
            addInst(NasmInst.Instruction.mov, processAddress(inst.getDest(), null), inst.getDestReg().toString());
    }

    @Override
    public void visit(Compare inst) {
        String left, right;
        left = processIntegerValue(inst.getLhs(), inst.getLhsReg());
        if(inst.getRhs() != null)
            right = processIntegerValue(inst.getRhs(), inst.getRhsReg());
        else
            right = "0";
        if(inst.getLhs() instanceof Immediate) {
            addInst(NasmInst.Instruction.mov, inst.getLhsReg().toString(), left);
            addInst(NasmInst.Instruction.cmp, inst.getLhsReg().toString(), right);
        }
        else
            addInst(NasmInst.Instruction.cmp, left, right);
    }

    @Override
    public void visit(Function inst)
    {
        curFunc = inst;
        addInst(null, inst.getName().toString(), null);
        int i = 0;
        addInst(NasmInst.Instruction.push, "rbp", null);
        addInst(NasmInst.Instruction.mov, "rbp", "rsp");
        if(inst.getUsedSlotNum() % 2 != 0)
            inst.addUsedSlotNumber(1);
        addInst(NasmInst.Instruction.sub, "rsp", String.valueOf(inst.getUsedSlotNum() * 8));
        int paramNumber = inst.getParameters().size();
        for(Parameter item : inst.getParameters()) {
            StackSlot slot = mapAddressToSlot(item.getAddress());
            if (i < ARGUMENT)
                addInst(NasmInst.Instruction.mov, slot.toString(), parameterRegName[i]);
            else {
                int paramPos = (paramNumber - i + 1) * 8;
                StackSlot slot1 = new StackSlot(paramPos, StackSlot.SlotType.qword);
                addInst(NasmInst.Instruction.mov, spareRegName[0], slot1.toString());
                addInst(NasmInst.Instruction.mov, slot.toString(), spareRegName[0]);
            }
            ++i;
        }
    }

    @Override
    public void visit(Jump inst) {
        addInst(NasmInst.Instruction.jmp, inst.getTarget().toString(), null);
    }

    @Override
    public void visit(Label inst) {
        addInst(null, inst.toString(), null);
    }

    @Override
    public void visit(Malloc inst) {
        //System.out.println(1);
        String size;
        if (inst.getSize() instanceof Immediate) {
            size = String.valueOf((((Immediate) inst.getSize()).getValue() + 1) * 8);
            addInst(NasmInst.Instruction.mov, "rdi", size);
            //System.out.println(nasmInsts.get(nasmInsts.size() - 1).toString());
            addInst(NasmInst.Instruction.call, "malloc", null);
            //System.out.println(nasmInsts.get(nasmInsts.size() - 1).toString());
        }
        else {
            size = processIntegerValue(inst.getSize(), inst.getSizeReg());
            addInst(NasmInst.Instruction.mov, "r15", size);
            addInst(NasmInst.Instruction.add, "r15", "1");
            addInst(NasmInst.Instruction.imul, "r15", "8");
            addInst(NasmInst.Instruction.mov, "rdi", "r15");
            addInst(NasmInst.Instruction.call, "malloc", null);
        }
        addInst(NasmInst.Instruction.mov, processAddress(inst.getReturnAddress(), null), "rax");
        //System.out.println(nasmInsts.get(nasmInsts.size() - 1).toString());
    }

    @Override
    public void visit(MemCopy inst) {
        addInst(NasmInst.Instruction.mov, processAddress(inst.getToAddress(), null), processIntegerValue(inst.getFromAddress(), inst.getDataReg()));
    }

    @Override
    public void visit(Move inst) {}

    @Override
    public void visit(Return inst) {
        if(inst.getValue() != null) {
            String returnIntegerValue = processIntegerValue(inst.getValue(), inst.getValueReg());
            addInst(NasmInst.Instruction.mov, "rax", returnIntegerValue);
        }
        addInst(NasmInst.Instruction.add, "rsp", String.valueOf(curFunc.getUsedSlotNum() * 8));
        addInst(NasmInst.Instruction.pop, "rbp", null);
        addInst(NasmInst.Instruction.ret, null, null);
    }

    @Override
    public void visit(Store inst) {
        if(inst.getData() != null) {
            String store = processIntegerValue(inst.getData(), inst.getDataReg());
            //System.out.println(1);
            addInst(NasmInst.Instruction.mov, processAddress((Address) inst.getAddress(), null), store);
        }
    }




    private void addInst(NasmInst.Instruction inst, String operand1, String operand2) {
        if(isInitialzeInst) {
            addDataInst(initializeInsts, inst, operand1, operand2);
            return;
        }
        if(inst == null)
            inst = NasmInst.Instruction.NULL;
        if(operand1 == null)
            operand1 = "NULL";
        if(operand2 == null)
            operand2 = "NULL";
        nasmInsts.add(new NasmInst(inst, operand1, operand2));
    }

    private void addDataInst(List<NasmInst> insts, NasmInst.Instruction inst, String operand1, String operand2) {
        if(inst == null)
            inst = NasmInst.Instruction.NULL;
        if(operand1 == null)
            operand1 = "NULL";
        if(operand2 == null)
            operand2 = "NULL";
        insts.add(new NasmInst(inst, operand1, operand2));
    }

    private StackSlot mapAddressToSlot(Address address) {
        StackSlot.SlotType type = StackSlot.SlotType.qword;
        StackSlot slot = new StackSlot(rspPosition, rspPosition + 8, type);
        addressStackSlotMap.put(address, slot);
        rspPosition += 8;
        return slot;
    }

    private void removeRedundantInst() {
        for(int i = 0; i < nasmInsts.size(); ++i)
            if(isRedundant(nasmInsts.get(i)))
                nasmInsts.remove(i);
    }

    private boolean isRedundant(NasmInst nasmInst) {
        if(nasmInst.getInst() == NasmInst.Instruction.mov &&
                nasmInst.getOperand1().equals(nasmInst.getOperand2()))
            return true;
        return false;
    }

    private void visitDivBinaryOp(BinaryOp inst) {
        if(inst.getLhs() instanceof Immediate)
            addInst(NasmInst.Instruction.mov, inst.getLhsReg().toString(), String.valueOf(((Immediate) inst.getLhs()).getValue()));
        else
            addInst(NasmInst.Instruction.mov, inst.getLhsReg().toString(), processAddress((Address)inst.getLhs(), null));
        addInst(NasmInst.Instruction.mov, inst.getDestReg().toString(), inst.getLhsReg().toString());
        if(inst.getRhs() instanceof Immediate)
            addInst(NasmInst.Instruction.mov, inst.getRhsReg().toString(), processIntegerValue(inst.getRhs(), null));
        else
            addInst(NasmInst.Instruction.mov, inst.getRhsReg().toString(), processAddress((Address)inst.getRhs(), null));
        addInst(NasmInst.Instruction.mov, "rdx", "0");
        addInst(NasmInst.Instruction.idiv, inst.getRhsReg().toString(), null);
        addInst(NasmInst.Instruction.mov, processAddress(inst.getDest(), null), inst.getDestReg().toString());
    }


    private String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        if(value.equals("\\n"))
            return String.valueOf(10);
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sbu.append((int)chars[i]).append(", ");
        }
        sbu.append('0');
        return sbu.toString();
    }

    private String processIntegerValue(IntegerValue value, PhysicalRegister pr) {
        String ret;
        if(value instanceof Address) {
            ret = processAddress((Address) value, pr);
            if(pr != null) {
                addInst(NasmInst.Instruction.mov, pr.toString(), ret);
                ret = pr.toString();
            }
        }
        else if(value instanceof Immediate) {
            ret = String.valueOf(((Immediate) value).getValue());
        }
        else
            ret = pr.toString();
        return ret;
    }

    private String processAddress(Address address, PhysicalRegister pr) {
        if (address.getBase() == null) {
            if(address.isGlobal())
                return "qword["+address.getName().toString() + "]";
            else if(!addressStackSlotMap.containsKey(address)) {
                StackSlot slot = mapAddressToSlot(address);
                addInst(NasmInst.Instruction.mov, pr.toString(), (address).getName().toString());
                addInst(NasmInst.Instruction.mov, slot.toString(), pr.toString());
                return slot.toString();
            }
            else
                return addressStackSlotMap.get(address).toString();
        }
        else {
            addInst(NasmInst.Instruction.mov, address.getBaseReg().toString(), processAddress(address.getBase(), null));
            if (address.getOffset() instanceof Immediate)
                return "qword [" +
                        address.getBaseReg().toString() +
                        " + " +
                        String.valueOf(((Immediate) address.getOffset()).getValue() * 8 + 8) + "]";
            else {
                String offset = processIntegerValue(address.getOffset(), address.getOffsetReg());
                addInst(NasmInst.Instruction.add, address.getOffsetReg().toString(), "1");
                return "qword [" + address.getBaseReg().toString() + " + " + offset + " * 8]";
            }
        }
    }
}
