package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.Type.BuiltInType;
import wtlcompiler.utility.location;

public class BoolConstNode extends ConstExprNode {
    private boolean bool;

    public BoolConstNode(location pos, boolean ans) {
        super(pos);
        bool = ans;
        setExprType(new BuiltInType("bool",1));
    }

     public boolean getBool() {
        return bool;
     }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
