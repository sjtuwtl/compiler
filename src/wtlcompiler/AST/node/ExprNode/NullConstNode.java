package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
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
}
