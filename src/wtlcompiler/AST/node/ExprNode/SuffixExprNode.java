package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.UnaryOp;
import wtlcompiler.utility.location;

public class SuffixExprNode extends UnaryExprNode {
    public SuffixExprNode(location pos, UnaryOp op, ExprNode expr) {
        super(pos, op, expr);
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
