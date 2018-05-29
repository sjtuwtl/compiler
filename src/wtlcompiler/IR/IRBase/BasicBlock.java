package wtlcompiler.IR.IRBase;

import wtlcompiler.AST.node.StmtNode.BlockNode;
import wtlcompiler.IR.IRInstruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicBlock {
    public enum BlockType {
        IF, WHILE, FOR, ELSE
    }

    private IRInstruction head;
    private IRInstruction tail;
    private BlockType blocktype;
    private List<IRInstruction> instructions;
    private Set<BasicBlock> succs = new HashSet<>();
    private Set<BasicBlock> prevs = new HashSet<>();

    public BasicBlock(IRInstruction head, IRInstruction tail, BlockType blocktype) {
        this.head = head;
        this.tail = tail;
        this.blocktype = blocktype;
        this.instructions = new ArrayList<>();
    }

    public BasicBlock() {
        instructions = new ArrayList<>();
    }

    public BasicBlock(List<IRInstruction> instructions) {
        this.instructions = instructions;
        this.head = instructions.get(0);
        this.tail = instructions.get(instructions.size() - 1);
    }

    public BlockType getBlocktype() {
        return blocktype;
    }

    public void setBlocktype(BlockType blocktype) {
        this.blocktype = blocktype;
    }

    public IRInstruction getHead() {
        return head;
    }

    public IRInstruction getTail() {
        return tail;
    }

    public void setHead(IRInstruction head) {
        this.head = head;
    }

    public void setTail(IRInstruction tail) {
        this.tail = tail;
    }

    public void addInstruction(IRInstruction instruction) {
        this.instructions.add(instruction);
    }

    public void addSucc(BasicBlock succ) {
        this.succs.add(succ);
    }

    public void addPrev(BasicBlock prev) {
        this.prevs.add(prev);
    }
}
