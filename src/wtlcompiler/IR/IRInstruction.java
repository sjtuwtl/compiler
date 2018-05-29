package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;

public abstract class IRInstruction {
    private Label label;
    private IRInstruction next;
    private IRInstruction prev;

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
}

