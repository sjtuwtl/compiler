package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.Type.BuiltInType;
import wtlcompiler.utility.location;

public abstract class ConditionExprNode extends ExprNode{
    private ExprNode left, right;

    public ConditionExprNode(location pos, ExprNode l, ExprNode r) {
        super(pos);
        left = l;
        right = r;
        setExprType(new BuiltInType("bool", 1));
    }

    public ExprNode getLeft() {
        return left;
    }

    public ExprNode getRight() {
        return right;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
