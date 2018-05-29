package wtlcompiler.IR;

import wtlcompiler.AST.node.ExprNode.MemberExprNode;
import wtlcompiler.IR.IRBase.IRInstTraversal;
import wtlcompiler.IR.Value.Address;

public class MemCopy extends IRInstruction{
    private Address fromAddress;
    private Address toAddress;

    public MemCopy(Label label, Address address1, Address address2) {
        super(label);
        fromAddress = address1;
        toAddress = address2;
    }

    public Address getFromAddress() {
        return fromAddress;
    }

    public Address getToAddress() {
        return toAddress;
    }

    @Override
    public String toString() {
        return "MemCopy: " + fromAddress.toString() + " to " + toAddress.toString();
    }

    @Override
    public void accept(IRInstTraversal visitor) {
        visitor.visit(this);
    }
}
