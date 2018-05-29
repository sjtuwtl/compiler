package wtlcompiler.AST.node;

import wtlcompiler.AST.tool.*;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.utility.location;

public abstract class ASTNode {
    private location pos;
    public ASTNode() {
        pos = new location(0,0);
    }

    public ASTNode(location loc) {
        pos = loc;
    }

    public location getLocation() {
        return pos;
    }

    public abstract void accept(ASTVisitor visitor);

    public abstract Object accept(IRTraversal visitor);

}
