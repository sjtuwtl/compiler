package wtlcompiler.AST.object;

import wtlcompiler.AST.node.ExprNode.ExprListNode;
import wtlcompiler.Type.Type;

public class FuncObject extends Object {
    private ExprListNode parameter;
    private Type returnType;

    public FuncObject(String name, ExprListNode param, Type type) {
        super(name);
        parameter = param;
        returnType = type;
    }

    public final ExprListNode getParameter() {
        return parameter;
    }

    public final Type getReturnType() {
        return returnType;
    }
}
