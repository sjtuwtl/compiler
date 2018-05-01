package wtlcompiler.AST.object;

import wtlcompiler.AST.node.DeclNode.FuncParamNode;
import wtlcompiler.Type.Type;

import java.util.List;

public class FuncDeclObject extends Object{
    private List<FuncParamNode> parameter;
    private Type returnType;
    private boolean isConstructor;

    public FuncDeclObject(String name, List<FuncParamNode> param, Type type) {
        super(name);
        parameter = param;
        returnType = type;
        isConstructor = false;
    }

    public FuncDeclObject(String name, List<FuncParamNode> param, Type type, boolean ch) {
        super(name);
        parameter = param;
        returnType = type;
        isConstructor = ch;
    }

    public List<FuncParamNode> getParameter() {
        return parameter;
    }

    public final Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public boolean isConstructor() {
        return isConstructor;
    }
}
