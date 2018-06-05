package wtlcompiler.Optimizer;

import wtlcompiler.IR.*;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.Register;
import wtlcompiler.IR.Value.VitualRegister;

import java.util.*;

public class LivenessAnalysis {
    private IRInstruction entry;
    private IRInstruction curEntry;
    private IRInstruction initializeEntry;
    private IRInstruction lastEntry;
    public Map<VitualRegister, Integer> map = new HashMap<>();

    public static class graphNode{
        public List<graphNode> neighbours = new LinkedList<>();
        public Integer color = -1;
        public String name;
        public Register register;
        public int degree = 0;


        public graphNode(Register register) {
            this.register = register;
            this.name = register.toString();
        }
        void add(graphNode n) {
            neighbours.add(n);
            ++degree;
        }

        void dec() {
            for (int i = 0; i < neighbours.size(); ++i)
                --neighbours.get(i).degree;
        }

        public String toString() {
            return name;
        }
    }

    public Map<String, graphNode> nodes = new HashMap<>();

    public LivenessAnalysis(IRInstruction entry, IRInstruction initializeEntry) {
        this.entry = entry;
        this.initializeEntry = initializeEntry;
        this.curEntry = entry;
    }

    public void process() {
        liveness(entry);
        liveness(initializeEntry);
    }


    public void liveness(IRInstruction entry) {
        List<graphNode> nodelist = new LinkedList<>();
        nodelist.clear();
        List<Register> registerlist = new LinkedList<>();
        registerlist.clear();
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
            curEntry.setUsedRegister();
            lastEntry = curEntry;
            if (curEntry.getNext() != null)
                curEntry.getNext().setPrev(curEntry);
            curEntry = curEntry.getNext();
        }

        Set<VitualRegister> in = new HashSet<>();
        Set<VitualRegister> out = new HashSet<>();

        boolean changed = true;
        while(changed) {
            changed = false;
            for (IRInstruction inst = lastEntry; inst != null; inst = inst.getPrev()) {
                in.clear();
                out.clear();
                in.addAll(inst.liveIn);
                out.addAll(inst.liveOut);
                inst.liveIn.clear();
                inst.liveOut.clear();

                /*System.out.println(inst.toString());
                System.out.println("def:");
                if (inst.getDefRegister() != null) System.out.println(inst.getDefRegister().toString());
                System.out.println("used: " + String.valueOf(inst.getUsedRegister().size()));
                for (Register item : inst.getUsedRegister())
                    if (item != null) System.out.println(item.toString());
                System.out.println(" ");*/

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
                else if (!(inst instanceof Return)) {
                    if (inst.getNext() != null) inst.liveOut.addAll(inst.getNext().liveIn);
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

        //need to link def with out;
        curEntry = entry;
        while (curEntry != null) {
            /*System.out.println(curEntry.toString());
            System.out.println("def:");
            if (curEntry.getDefRegister() != null) System.out.println(curEntry.getDefRegister().toString());
            System.out.println("used: " + String.valueOf(curEntry.getUsedRegister().size()));
            for (Register item : curEntry.getUsedRegister())
                if (item != null) System.out.println(item.toString());
            System.out.println("in:");
            for (VitualRegister item : curEntry.liveIn)
                System.out.println(item.toString());
            System.out.println("out:");
            for (VitualRegister item : curEntry.liveOut)
                System.out.println(item.toString());
            System.out.println("  ");*/
            if (curEntry.getDefRegister() != null) {
                graphNode defnode = null;
                if (!registerlist.contains(curEntry.getDefRegister())) {
                    defnode = new graphNode(curEntry.getDefRegister());
                    nodelist.add(defnode);
                    registerlist.add(curEntry.getDefRegister());
                }
                else
                    for (graphNode item : nodelist)
                        if (item.register.equals(curEntry.getDefRegister())) {
                            defnode = item;
                            break;
                        }

                for (VitualRegister item : curEntry.liveOut){
                    graphNode outnode = null;
                    if (!registerlist.contains(item)) {
                        outnode = new graphNode(item);
                        nodelist.add(outnode);
                        registerlist.add(item);
                    }
                    else
                        for (graphNode item2 : nodelist)
                            if (item2.register.equals(item)) {
                                outnode = item2;
                                break;
                            }
                    if (defnode != outnode && !defnode.neighbours.contains(outnode)) {
                        defnode.add(outnode);
                        outnode.add(defnode);
                    }

                }
            }
            curEntry = curEntry.getNext();
        }

       /* for (graphNode item1 : nodelist) {
            System.out.println(item1.degree);
            for (graphNode item2 : item1.neighbours)
                System.out.println(item1.toString() + "------" + item2.toString());
            System.out.println("");
        }*/
        boolean [] iscolored = new boolean[12];
        for (int i = 0; i < nodelist.size() ; ++i) {
            graphNode node = null;
            int mindegree = 2147483647;
            for (graphNode item : nodelist)
                if (item.degree <= mindegree) {
                    node = item;
                    mindegree = item.degree;
                }
            for (int j = 0; j < 12; ++j)  iscolored[j] = true;
            for (graphNode item : node.neighbours) {
                if (item.color != -1) iscolored[item.color] = false;
            }
            for (int j = 0; j < 12; ++j)
                if (iscolored[j]) {
                    node.color = j;
                    node.dec();
                    node.degree = 2147483647;
                    break;
                }
        }

        //for (graphNode item : nodelist)
        //    System.out.println(item.name + "  " + String.valueOf(item.color));

        for (graphNode item : nodelist)
            map.put((VitualRegister)item.register, item.color);

    }
}
