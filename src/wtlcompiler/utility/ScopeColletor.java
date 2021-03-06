package wtlcompiler.utility;

import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.Type.*;

import java.util.Stack;

public class ScopeColletor implements ASTVisitor {
    private Scope currentScope;
    private Stack<Scope> scopeStack = new Stack<>();
    private ErrorHandle errorHandle;

    public ScopeColletor(Scope topScope, ErrorHandle handle) {
        setCurrentScope(topScope);
        errorHandle = handle;
    }

    public void process(ProgNode progNode) {
        visit(progNode);
    }

    @Override
    public void visit(ProgNode node) {
        if(node == null) return;
        for(DeclNode item : node.getDeclares())
            visit(item);
        node.setScope(currentScope);
    }

    @Override
    public void visit(DeclNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node)
    {
        if(!currentScope.isTop())
            errorHandle.addError(node.getLocation(), "class defination should be at top scope");
        currentScope.addType(node);
        node.setType((ClassType)currentScope.findType(node.getName()));
        Scope scope = new Scope(currentScope);
        scope.setClass(true);
        node.setInternalScope(scope);
        setCurrentScope(scope);
        for(FuncDeclNode item : node.getMemberFunction())
            visit(item);
        int i = 0;
        for(VarDeclNode item : node.getMemberVarible()) {
            visit(item);
            item.setMember(true);
            item.setMemberNum(i++);
        }
        exitCurrentScope();
    }

    @Override
    public void visit(FuncDeclNode node) {
        if(!currentScope.isTop() && !currentScope.isClass())
            errorHandle.addError(node.getLocation(),
                    "function defination should be in top scope or in a class");
        node.setExternalScope(currentScope);
        Scope scope = new Scope(currentScope);
        scope.setFunction(true);
        node.setInternalScope(scope);
        setCurrentScope(scope);
        for(FuncParamNode item : node.getParameter())
            visit(item);
        visit(node.getBlock());
        node.setInternalScope(node.getBlock().getScope());
        exitCurrentScope();
    }

    @Override public void visit(VarDeclNode node) { }

    @Override public void visit(FuncParamNode node) { }

    @Override public void visit(ExprNode node) { }

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

    @Override public void visit(ThisExprNode node) { }

    @Override public void visit(StmtNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        Scope scope = new Scope(currentScope);
        scope.setFunction(currentScope.isFunction());
        scope.setLoop(currentScope.isLoop());
        setCurrentScope(scope);
        node.setScope(scope);
        for(StmtNode item : node.getStatements())
            visit(item);
        exitCurrentScope();
    }

    @Override public void visit(BreakNode node) { }

    @Override public void visit(ContinueNode node) { }

    @Override public void visit(ReturnNode node) { }

    @Override
    public void visit(ForNode node) {
        node.setExternalScope(currentScope);
        Scope scope = new Scope(currentScope);
        scope.setLoop(true);
        scope.setFunction(currentScope.isFunction());
        node.setInternalScope(scope);
        setCurrentScope(scope);
        visit(node.getBlock());
        exitCurrentScope();
    }

    @Override
    public void visit(IfNode node) {
        node.setExternalScope(currentScope);
        Scope scope = new Scope(currentScope);
        scope.setLoop(currentScope.isLoop());
        scope.setFunction(currentScope.isFunction());
        node.setInternalScope(scope);
        setCurrentScope(scope);
        visit(node.getState());
        if(node.getElsestate() != null)
            visit(node.getElsestate());
        exitCurrentScope();
    }

    @Override
    public void visit(WhileNode node) {
        node.setExternalScope(currentScope);
        Scope scope = new Scope(currentScope);
        scope.setLoop(true);
        scope.setFunction(currentScope.isFunction());
        node.setInternalScope(scope);
        setCurrentScope(scope);
        visit(node.getBlock());
        exitCurrentScope();
    }

    @Override public void visit(ExprStatNode node) { }

    @Override public void visit(Type type) { }

    private void setCurrentScope(Scope current) {
        currentScope = current;
        scopeStack.push(currentScope);
    }
    private void exitCurrentScope() {
        currentScope = currentScope.getParent();
        scopeStack.pop();
    }

}
