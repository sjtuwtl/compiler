package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.Type.Type;
import wtlcompiler.utility.location;

import java.util.List;

public class CreatorExprNode extends ExprNode{
    private Type type;
    private List<ExprNode> expresses;
    private int size;

    public CreatorExprNode(location pos, Type t, List<ExprNode> exprs, int s) {
        super(pos);
        type = t;
        expresses = exprs;
        size = s;
    }

    @Override
    public Type getExprType() {
        return super.getExprType();
    }

    @Override
    public void setExprType(Type exprTy) {
        super.setExprType(exprTy);
    }

    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
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
