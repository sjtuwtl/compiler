package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.location;

public class NewExprNode extends ExprNode {
    private CreatorExprNode creatorNode;

    public NewExprNode(location pos, CreatorExprNode creator) {
        super(pos);
        creatorNode = creator;
    }

    public CreatorExprNode getCreatorNode() {
        return creatorNode;
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
