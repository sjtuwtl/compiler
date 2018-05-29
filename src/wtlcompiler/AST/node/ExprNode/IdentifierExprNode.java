package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.Name;
import wtlcompiler.utility.location;

public class IdentifierExprNode extends  ExprNode{
    private Name name;

    public IdentifierExprNode(location pos, String na) {
        super(pos);
        name = Name.getName(na);
        setIsLeftvalue(true);
    }

    public Name getName() {
        return name;
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
