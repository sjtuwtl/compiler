package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.location;

public class ThisExprNode extends ExprNode {
    private String name;

    public ThisExprNode(location pos, String na) {
        super(pos);
        name = na;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
