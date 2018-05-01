package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.Type.BuiltInType;
import wtlcompiler.utility.location;

public class StringConstNode extends ConstExprNode{
    private String str;

    public StringConstNode(location pos, String s) {
        super(pos);
        str = s;
        setExprType(new BuiltInType("string", 1));
    }

    public String getStr() {
        return str;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
