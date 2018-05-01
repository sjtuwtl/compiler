package wtlcompiler.AST.node;

import wtlcompiler.AST.node.DeclNode.DeclNode;
import wtlcompiler.utility.location;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.Scope;

import java.util.List;

public class ProgNode extends ASTNode{
    private  List<DeclNode> declares;
    public ProgNode(location pos, List<DeclNode> decl) {
        super(pos);
        declares = decl;
    }

    public final List<DeclNode> getDeclares() {
        return declares;
    }

    public void addNode(DeclNode decl) {
        declares.add(decl);
    }

    public void addNode(List<DeclNode> declList) {
        for (DeclNode decl : declList) {
            addNode(decl);
        }
    }

    private Scope scope;

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
