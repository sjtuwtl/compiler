package wtlcompiler.AST.node.ExprNode;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.utility.location;

public class ArrayExprNode extends ExprNode {
    private ExprNode array, index;

    public ArrayExprNode(location pos, ExprNode arr, ExprNode ind) {
        super(pos);
        array = arr;
        index = ind;
        setIsLeftvalue(true);
    }

    public ExprNode getArray() {
        return array;
    }

    public ExprNode getIndex() {
        return index;
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
