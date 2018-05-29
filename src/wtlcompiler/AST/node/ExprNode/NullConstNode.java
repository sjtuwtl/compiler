package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.Type.BuiltInType;
import wtlcompiler.utility.location;

public class NullConstNode extends ConstExprNode{
    public NullConstNode(location pos) {
        super(pos);
        setExprType(new BuiltInType("null",0));
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
