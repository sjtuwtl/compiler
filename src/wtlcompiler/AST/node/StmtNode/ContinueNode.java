package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.utility.location;
import wtlcompiler.AST.tool.ASTVisitor;

public class ContinueNode extends StmtNode{
    public ContinueNode(location pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public IRInstruction accept(IRTraversal visitor) {
        visitor.visit(this);
        return null;
    }
}