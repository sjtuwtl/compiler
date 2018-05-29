package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.AST.node.ExprNode.ExprNode;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.utility.location;
import wtlcompiler.AST.tool.ASTVisitor;

public class ExprStatNode extends StmtNode{
    private ExprNode express;
    public ExprStatNode(location pos, ExprNode expr) {
        super(pos);
        express = expr;
    }

    public final ExprNode getExpress() {
        return express;
    }

    @Override
    public  void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public IRInstruction accept(IRTraversal visitor) {
        visitor.visit(this);
        return null;
    }
}
