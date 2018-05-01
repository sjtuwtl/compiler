package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.AST.node.ExprNode.ExprNode;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.Type.ArrayType;
import wtlcompiler.utility.Scope;
import wtlcompiler.utility.location;

public class WhileNode extends StmtNode{
    private ExprNode condition;
    private StmtNode block;
    private Scope internalScope, externalScope;

    public WhileNode(location pos, ExprNode cond, StmtNode blo) {
        super(pos);
        condition = cond;
        block = blo;
    }

    public ExprNode getCondition() {
        return condition;
    }

    public StmtNode getBlock() {
        return block;
    }

    public void setInternalScope(Scope internalScope) {
        this.internalScope = internalScope;
    }

    public void setExternalScope(Scope externalScope) {
        this.externalScope = externalScope;
    }

    public Scope getInternalScope() {
        return internalScope;
    }

    public Scope getExternalScope() {
        return externalScope;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
