package wtlcompiler.AST.node.DeclNode;

import wtlcompiler.AST.object.ParameterObject;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.utility.location;

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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
