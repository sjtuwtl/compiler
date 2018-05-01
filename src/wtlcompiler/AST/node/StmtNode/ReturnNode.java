package wtlcompiler.AST.node.StmtNode;

import wtlcompiler.AST.node.ExprNode.ExprNode;
import wtlcompiler.utility.location;
import wtlcompiler.AST.tool.ASTVisitor;

public class ReturnNode extends StmtNode{
    private ExprNode express;

    public ReturnNode(location pos, ExprNode expr) {
        super(pos);
        express = expr;
    }

    public ExprNode getExpress() {
        return express;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}