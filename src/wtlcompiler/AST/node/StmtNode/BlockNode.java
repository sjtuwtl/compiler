package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.location;
import wtlcompiler.utility.Scope;

import java.util.List;

public class BlockNode extends StmtNode {
    private List<StmtNode> statements;
    private Scope scope;

    public BlockNode(location pos, List<StmtNode> stmt) {
        super(pos);
        statements = stmt;
    }

    public final List<StmtNode> getStatements() {
        return statements;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
