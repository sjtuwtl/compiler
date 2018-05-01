package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.location;

public class AssignExprNode extends ExprNode{
    private ExprNode left, right;

    public AssignExprNode(location pos, ExprNode lhs, ExprNode rhs) {
        super(pos);
        left = lhs;
        right = rhs;
    }

    public final ExprNode getRight() {
        return right;
    }

    public final ExprNode getLeft() {
        return left;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
