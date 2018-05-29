package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.location;

import java.util.List;

public class ExprListNode extends ExprNode{
    private List<ExprNode> expresses;
    public ExprListNode(location pos, List<ExprNode> exprs) {
        super(pos);
        expresses = exprs;
    }

    public void addExpr(ExprNode node) {
        expresses.add(node);
    }

    public List<ExprNode> getExpresses() {
        return expresses;
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
