package wtlcompiler.Optimizer;

import wtlcompiler.IR.*;
import wtlcompiler.IR.Value.Register;
import wtlcompiler.IR.Value.VitualRegister;

import java.util.HashSet;
import java.util.Set;

public class LivenessAnalysis {
    private IRInstruction entry;
    private IRInstruction curEntry;
    private IRInstruction lastEntry;

    public LivenessAnalysis(IRInstruction entry) {
        this.entry = entry;
        this.curEntry = entry;
    }

    private void process() {
        curEntry = entry;
        while (curEntry != null) {
            if (curEntry.liveIn != null) {
                curEntry.liveIn.clear();
                curEntry.liveOut.clear();
            }
            else {
                curEntry.liveIn = new HashSet<>();
                curEntry.liveOut = new HashSet<>();
            }
            lastEntry = curEntry;
            curEntry = curEntry.getNext();
        }

        Set<VitualRegister> in = new HashSet<>();
        Set<VitualRegister> out = new HashSet<>();

        boolean changed = true;
        while(changed) {
            for (IRInstruction inst = lastEntry; inst != null; inst.getPrev()) {
                in.clear();
                out.clear();
                in.addAll(inst.liveIn);
                out.addAll(inst.liveOut);
                inst.liveIn.clear();
                inst.liveOut.clear();

                if (inst instanceof Branch) {
                    if (((Branch) inst).getIfTure() != null)
                        inst.liveOut.addAll(((Branch) inst).getIfTure().liveIn);
                    if (((Branch) inst).getIfFalse() != null)
                        inst.liveOut.addAll(((Branch) inst).getIfFalse().liveIn);
                }
                else if (inst instanceof Jump) {
                    if (((Jump) inst).getTarget() != null)
                        inst.liveOut.addAll(((Jump) inst).getTarget().liveIn);
                }
                else if (inst instanceof Call) {

                }
                else if (!(inst instanceof Return)) {
                    inst.liveOut.addAll(inst.getNext().liveOut);
                }

                for (Register item : inst.usedRegister)
                    if (item instanceof VitualRegister)
                        inst.liveIn.add((VitualRegister) item);
                inst.liveIn.addAll(inst.liveOut);
                Register defRegister = inst.getDefRegister();
                if (defRegister instanceof VitualRegister && !inst.usedRegister.contains(defRegister))
                    inst.liveIn.remove(defRegister);

                if (!inst.liveIn.equals(in) || !inst.liveOut.equals(out))
                    changed = true;

            }
        }
    }
}
