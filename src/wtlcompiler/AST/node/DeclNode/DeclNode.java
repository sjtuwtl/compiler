package wtlcompiler.AST.node.DeclNode;

import wtlcompiler.AST.node.StmtNode.StmtNode;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.utility.location;

public abstract class DeclNode extends StmtNode{
    protected boolean is_func;
    protected boolean isBuiltIn;

    public DeclNode(location pos) {
        super(pos);
        is_func = false;
        isBuiltIn = false;
    }

    public DeclNode(location pos, boolean isBuiltIn) {
        super(pos);
        is_func = false;
        this.isBuiltIn = isBuiltIn;
    }

    public void setIsFunction(boolean is_func) {
        this.is_func = is_func;
    }

    public boolean isFunction() {
        return is_func;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }
}
