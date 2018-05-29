package wtlcompiler.AST.node.DeclNode;

import wtlcompiler.AST.node.StmtNode.BlockNode;
import wtlcompiler.AST.object.FuncDeclObject;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.IR.IRBase.BasicBlock;
import wtlcompiler.utility.location;
import wtlcompiler.utility.Name;
import wtlcompiler.Type.*;
import wtlcompiler.utility.Scope;
import wtlcompiler.IR.IRBase.IRTraversal;
import wtlcompiler.IR.Function;

import java.util.List;
import java.util.ArrayList;

public class FuncDeclNode extends DeclNode{
    private FuncDeclObject function;
    private BlockNode block;
    private Scope externalScope, internalScope;

    public FuncDeclNode(boolean isBuiltIn, location pos, FuncDeclObject func, BlockNode blo) {
        super(pos, isBuiltIn);
        function = func;
        block = blo;
        setIsFunction(true);
    }

    public FuncDeclNode(location pos, FuncDeclObject func, BlockNode blo) {
        super(pos);
        function = func;
        block = blo;
        setIsFunction(true);
    }

    public FuncDeclNode(String name) {
        super(new location(0,0));
        function = new FuncDeclObject(name, new ArrayList<>(), new BuiltInType("int", 4));
        block = null;
        setIsFunction(true);
    }

    public FuncDeclObject getFunction() {
        return function;
    }

    public BlockNode getBlock() {
        return block;
    }

    public void setReturnType(Type type) {
        function.setReturnType(type);
    }

    public Type getReturnType() {
        return function.getReturnType();
    }

    public boolean isConstructor() {
        return function.isConstructor();
    }

    public List<FuncParamNode> getParameter() {
        return function.getParameter();
    }

    public Scope getExternalScope() {
        return externalScope;
    }

    public Scope getInternalScope() {
        return internalScope;
    }

    public void setExternalScope(Scope externalScope) {
        this.externalScope = externalScope;
    }

    public void setInternalScope(Scope internalScope) {
        this.internalScope = internalScope;
    }

    @Override
    public Name getName()
    {
        return function.getName();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Function accept(IRTraversal visitor)
    {
        return visitor.visit(this);
    }
}
