package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.Immediate;
import wtlcompiler.Type.BuiltInType;
import wtlcompiler.utility.location;

public class IntConstNode extends ConstExprNode {
    private int integer;

    public IntConstNode(location pos, int ans) {
        super(pos);
        integer = ans;
        setExprType(new BuiltInType("int", 1));
    }

    public int getInteger() {
        return integer;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Immediate accept(IRTraversal visitor) {
        return visitor.visit(this);
    }
}
