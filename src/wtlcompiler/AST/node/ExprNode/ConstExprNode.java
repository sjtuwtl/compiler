package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.location;

public class ConstExprNode extends ExprNode {
    public ConstExprNode(location pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public IntegerValue accept(IRTraversal visitor)
    {
        return visitor.visit(this);
    }
}
