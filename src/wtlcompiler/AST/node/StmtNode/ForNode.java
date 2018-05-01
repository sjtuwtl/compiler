package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.AST.node.ExprNode.ExprNode;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.Scope;
import wtlcompiler.utility.location;

public class ForNode extends StmtNode{
    private ExprNode beginCondition, endCondition, update;
    private StmtNode block;
    private Scope externalScope, internalScope;

    public ForNode(location pos, ExprNode begin, ExprNode end, ExprNode upd, StmtNode blo) {
        super(pos);
        beginCondition = begin;
        endCondition = end;
        update = upd;
        block = blo;
    }

    public ExprNode getBeginCondition() {
        return beginCondition;
    }

    public ExprNode getEndCondition() {
        return endCondition;
    }

    public ExprNode getUpdate() {
        return update;
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
