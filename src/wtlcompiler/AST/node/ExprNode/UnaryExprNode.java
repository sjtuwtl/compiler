package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.UnaryOp;
import wtlcompiler.utility.location;

public abstract class UnaryExprNode extends ExprNode{
    private UnaryOp option;
    private ExprNode expr;

    public UnaryExprNode(location pos, UnaryOp op, ExprNode e) {
        super(pos);
        option = op;
        expr = e;
    }

    public UnaryOp getOption() {
        return option;
    }

    public ExprNode getExpr() {
        return expr;
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
