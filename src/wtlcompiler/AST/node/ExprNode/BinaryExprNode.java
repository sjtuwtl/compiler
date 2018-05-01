package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.BinaryOp;
import wtlcompiler.utility.location;

public class BinaryExprNode extends ExprNode {
    private ExprNode l, r;
    private BinaryOp op;

    public BinaryExprNode(location pos, ExprNode lhs, ExprNode rhs, BinaryOp option) {
        super(pos);
        l = lhs;
        r = rhs;
        op = option;
    }

    public BinaryOp getOp() {
        return op;
    }

    public ExprNode getL() {
        return l;
    }

    public ExprNode getR() {
        return r;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
