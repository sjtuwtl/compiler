package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.node.ASTNode;
import wtlcompiler.utility.location;
import wtlcompiler.Type.Type;

public abstract class ExprNode extends ASTNode{
    private boolean isLeftvalue;
    private Type exprType;

    public ExprNode(location pos) {
        super(pos);
        isLeftvalue = false;
    }

    public void setIsLeftvalue(boolean value) {
        isLeftvalue = value;
    }

    public boolean isLeftvalue() {
        return isLeftvalue;
    }

    public void setExprType(Type exprTy) {
        exprType = exprTy;
    }

    public Type getExprType() {
        return exprType;
    }
}
