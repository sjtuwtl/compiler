package wtlcompiler.IR;

import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Register;

import java.util.List;

public class Jump extends Terminator{
    private Label target;

    public Jump(Label label, Label target) {
        super(label);
        this.target = target;
    }

    public Label getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "Jump: " + target.toString();
    }

    @Override
    public Register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() { }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
