package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
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

    @Override
    public IntegerValue accept(IRTraversal visitor) {
        return visitor.visit(this);
    }
}
