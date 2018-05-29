package wtlcompiler.AST.node.DeclNode;

import wtlcompiler.AST.object.ParameterObject;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.location;
import wtlcompiler.Type.*;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.utility.Name;


public class FuncParamNode extends VarDeclNode{
    private ParameterObject parameter;
    public FuncParamNode(location pos, ParameterObject param) {
        super(pos, param, null);
        parameter = param;
    }

    public ParameterObject getParameter() {
        return parameter;
    }

    @Override
    public Name getName() {
        return parameter.getName();
    }

    @Override
    public Address accept(IRTraversal visitor) {
        visitor.visit(this);
        return null;
    }

    @Override
    public Type getType() {
        return parameter.getType();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
