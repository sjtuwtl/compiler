package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.Immediate;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.location;

public class AndExprNode extends  ConditionExprNode {
    public AndExprNode(location pos, ExprNode left, ExprNode right) {
        super(pos, left, right);
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
