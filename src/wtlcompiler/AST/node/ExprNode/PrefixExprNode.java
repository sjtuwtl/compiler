package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.UnaryOp;
import wtlcompiler.utility.location;

public class PrefixExprNode extends UnaryExprNode {
    public PrefixExprNode(location pos, UnaryOp op, ExprNode expr) {
        super(pos, op, expr);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
