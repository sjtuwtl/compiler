package wtlcompiler.utility;

import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.Type.*;

import java.lang.Exception;
import java.util.Stack;

public class FunctionCollector implements ASTVisitor {
    private Scope current;
    private Stack<Scope> scopeStack = new Stack<>();
    private ErrorHandle errorHandle;

    public FunctionCollector(Scope top, ErrorHandle handle) {
        errorHandle = handle;
    }

    public void process(ProgNode progNode) {
        visit(progNode);
    }

    @Override
    public void visit(ProgNode node) {
        if(node == null) return;
        setCurrent(node.getScope());
        for(DeclNode item : node.getDeclares()) {
            if (item instanceof VarDeclNode) continue;
            visit(item);
        }
        node.setScope(current);
    }

    @Override
    public void visit(DeclNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        if(node == null) return;
        setCurrent(node.getInternalScope());
        for(FuncDeclNode item : node.getMemberFunction())
            visit(item);
        for(VarDeclNode item : node.getMemberVarible())
            visit(item);
        node.setInternalScope(current);
        exitCurrent();
    }

    @Override
    public void visit(FuncDeclNode node) {
        if(node == null) return;
        setCurrent(node.getInternalScope());
        try {
            visit(node.getReturnType());
        }
        catch (Exception e) {
            errorHandle.addError(node.getLocation(), e.getMessage());
        }
        if(!(node.getReturnType() instanceof ArrayType))
            node.setReturnType(current.findType(node.getReturnType().getTypeName()));
        for(FuncParamNode item : node.getParameter())
            visit(item);
        node.getExternalScope().addNode(node);
        node.setInternalScope(current);
        exitCurrent();
    }

    @Override
    public void visit(VarDeclNode node) {
        if(node == null) return;
        try {
            visit(node.getType());
        }
        catch (Exception e) {
            errorHandle.addError(node.getLocation(), e.getMessage());
        }
        if(node.getType() instanceof ClassType)
            node.setType(current.findType(node.getType().getTypeName()));
        current.addNode(node);
    }

    @Override
    public void visit(FuncParamNode node) {
        if(node == null) return;
        node.setScope(current);
        try {
            visit(node.getType());
        }
        catch(Exception e) {
            errorHandle.addError(node.getLocation(), e.getMessage());
        }
        if(node.getType() instanceof ClassType)
            node.setType(current.findType(node.getType().getTypeName()));
        current.addNode(node);
    }

    @Override
    public void visit(ExprNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override public void visit(AndExprNode node) { }

    @Override public void visit(ArrayExprNode node) { }

    @Override public void visit(AssignExprNode node) { }

    @Override public void visit(BinaryExprNode node) { }

    @Override public void visit(BoolConstNode node) { }

    @Override public void visit(CallExprNode node) { }


    @Override public void visit(ExprListNode node) { }

    @Override public void visit(IdentifierExprNode node) { }

    @Override public void visit(IntConstNode node) { }

    @Override public void visit(MemberExprNode node) { }

    @Override public void visit(NewExprNode node) { }

    @Override public void visit(CreatorExprNode node) { }

    @Override public void visit(NullConstNode node) { }

    @Override public void visit(OrExprNode node) { }

    @Override public void visit(PrefixExprNode node) { }

    @Override public void visit(StringConstNode node) { }

    @Override public void visit(SuffixExprNode node) { }

    @Override
    public void visit(ThisExprNode node) {
        //TODO
    }

    @Override
    public void visit(StmtNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override public void visit(BlockNode node) { }

    @Override public void visit(BreakNode node) { }

    @Override public void visit(ContinueNode node) { }

    @Override public void visit(ReturnNode node) { }

    @Override
    public void visit(ForNode node) {
        if(node == null) return;
        setCurrent(node.getInternalScope());
        visit(node.getBlock());
        node.setInternalScope(current);
        exitCurrent();
    }

    @Override
    public void visit(IfNode node) {
        if(node == null) return;
        setCurrent(node.getInternalScope());
        visit(node.getState());
        if(node.getElsestate() != null)
            visit(node.getElsestate());
        node.setInternalScope(current);
        exitCurrent();
    }

    @Override
    public void visit(WhileNode node) {
        if(node == null) return;
        setCurrent(node.getInternalScope());
        visit(node.getBlock());
        node.setInternalScope(current);
        exitCurrent();
    }

    @Override public void visit(ExprStatNode node) { }

    @Override
    public void visit(Type type) throws Exception {
        if(type instanceof ArrayType) {
            if(!current.containsType(((ArrayType) type).getBaseType().getTypeName())) {
                throw new RuntimeException("type " + ((ArrayType) type).getBaseType().getTypeName().toString()
                        + " have not been declared");
            }
        }
        if(!current.containsType(type.getTypeName()))
            throw new RuntimeException("type " + type.getTypeName().toString() + " have not been declared");
    }

    private void setCurrent(Scope _currentScope) {
        current = _currentScope;
        scopeStack.push(_currentScope);
    }

    private void exitCurrent() {
        scopeStack.pop();
        current = scopeStack.peek();
    }

}
