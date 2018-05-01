package wtlcompiler.AST.node.DeclNode;

import wtlcompiler.AST.node.StmtNode.StmtNode;
import wtlcompiler.utility.location;

public abstract class DeclNode extends StmtNode{
    protected boolean is_func;
    public DeclNode(location pos) {
        super(pos);
        is_func = false;
    }

    public void setIsFunction(boolean is_func) {
        this.is_func = is_func;
    }

    public boolean isFunction() {
        return is_func;
    }
}
