package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.AST.node.ASTNode;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.utility.location;
import wtlcompiler.utility.Name;

public abstract class StmtNode extends ASTNode{
    public StmtNode(location pos) {
        super(pos);
    }

    public Name getName() {
        return Name.getName("Stmt");
    }

    @Override
    public IRInstruction accept(IRTraversal visitor) {
        return null;
    }
}
