package wtlcompiler.AST.node.ExprNode;

import jdk.nashorn.internal.codegen.CompilerConstants;
import wtlcompiler.AST.node.DeclNode.FuncDeclNode;
import wtlcompiler.AST.object.FuncDeclObject;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.Name;
import wtlcompiler.utility.location;

public class CallExprNode extends ExprNode {
    private Name funcName;
    private ExprListNode parameter;
    private FuncDeclNode function;

    public CallExprNode(location pos, String name, ExprListNode param) {
        super(pos);
        funcName = Name.getName(name);
        parameter = param;
    }

    public Name getFuncName() {
        return funcName;
    }

    public ExprListNode getParameter() {
        return parameter;
    }

    public void setFuncName(Name funcName) {
        this.funcName = funcName;
    }

    public void setFunction(FuncDeclNode function) {
        this.function = function;
    }

    public FuncDeclNode getFunction() {
        return function;
    }

    public void addParam(ExprNode node, int pos) {
        this.parameter.addExpr(node, pos);
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
