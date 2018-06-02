package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.*;
import wtlcompiler.IR.IRBase.IRInstTraversal;

public class Label extends IRInstruction{
    private BasicBlock block;
    private String name;
    private static int count = 0;

    public Label(String na) {
        block = new BasicBlock();
        if (na == null) name = String.valueOf(count++); else name = na;
    }

    public BasicBlock getBlock() {
        return block;
    }

    public String getName() {
        return name;
    }

    public void addInst(IRInstruction inst) {
        block.addInstruction(inst);
    }

    @Override
    public String toString() {
        return "Label " + name;
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
