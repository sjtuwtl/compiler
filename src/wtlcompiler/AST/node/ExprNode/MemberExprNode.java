package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.Name;
import wtlcompiler.utility.location;

public class MemberExprNode extends ExprNode {
    private ExprNode express;
    private CallExprNode functionCall;
    private String identifier;
    private boolean isFunctionCall;

    public MemberExprNode(location pos, ExprNode expr, CallExprNode call) {
        super(pos);
        express = expr;
        functionCall = call;
        setIsLeftvalue(true);
        isFunctionCall = true;
    }

    public MemberExprNode(location pos, ExprNode expr, String id) {
        super(pos);
        express = expr;
        identifier = id;
        setIsLeftvalue(true);
        isFunctionCall = false;
    }

    public ExprNode getExpress() {
        return express;
    }

    public CallExprNode getFunctionCall() {
        return functionCall;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isFunctionCall() {
        return isFunctionCall;
    }

    public Name getName() {
        if (this.isFunctionCall) return functionCall.getFuncName();
        else return Name.getName(identifier);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public IntegerValue accept(IRTraversal visitor) {
        return visitor.visit(this);
    }
}
