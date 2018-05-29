package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.location;

public class ThisExprNode extends ExprNode {
    private String name;

    public ThisExprNode(location pos, String na) {
        super(pos);
        name = na;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public IntegerValue accept(IRTraversal visitor) {
        return visitor.visit(this);
    }
}
