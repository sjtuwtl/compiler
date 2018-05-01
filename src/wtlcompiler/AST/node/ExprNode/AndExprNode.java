package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.location;

public class AndExprNode extends  ConditionExprNode {
    public AndExprNode(location pos, ExprNode left, ExprNode right) {
        super(pos, left, right);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
