package wtlcompiler.utility;

import wtlcompiler.AST.object.FuncDeclObject;
import wtlcompiler.AST.tool.ASTVisitor;
import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.Type.*;

import java.util.List;
import java.util.Stack;

public class Semantic implements ASTVisitor {
    private Scope currentScope;
    private Stack<Scope> scopeStack = new Stack<>();
    private ErrorHandle errorHandle;
    private FuncDeclNode currentFunction;
    private ClassDeclNode currentClass;

    public Semantic(Scope topScope, ErrorHandle handler) {
        errorHandle = handler;
    }

    public void process(ProgNode progNode) {
        visit(progNode);
    }

    @Override
    public void visit(ProgNode node) {
        if(node == null) return;
        setCurrentScope(node.getScope());
        if(!currentScope.containsNode(Name.getName("main")) ||
                !currentScope.findNode(Name.getName("main")).isFunction())
            errorHandle.addError(new location(0,0), "no main function");
        if(((FuncDeclNode)currentScope.findNode(Name.getName("main"))).getReturnType().getTypeName() != Name.getName("int"))
            errorHandle.addError(node.getLocation(),
                    "the function 'main' should return an integer");
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
    public void visit(ClassDeclNode node) {
        if(node == null) return;
        currentClass = node;
        setCurrentScope(node.getInternalScope());
        for(VarDeclNode item : node.getMemberVarible())
            visit(item);
        for(FuncDeclNode item : node.getMemberFunction())
            visit(item);
        node.setInternalScope(currentScope);
        exitCurrentScope();
        currentClass = null;
    }

    @Override
    public void visit(FuncDeclNode node) {
        if(node == null) return;
        currentFunction = node;
        setCurrentScope(node.getInternalScope());
        if(node.isConstructor() && (node.getName() != currentClass.getName()))
            errorHandle.addError(node.getLocation(), "name of the constructor should be the name of the class");
        for(FuncParamNode item : node.getParameter())
            visit(item);
        visit(node.getBlock());
        node.setInternalScope(currentScope);
        exitCurrentScope();
        currentFunction = null;
    }

    @Override
    public void visit(VarDeclNode node) {
        if(node == null) return;
        node.setScope(currentScope);
        visit(node.getValue());
        if(!(currentScope.isClass())) {
            if (node == null) return;
            try {
                visit(node.getType());
            }
            catch (Exception e) {
                errorHandle.addError(node.getLocation(), e.getMessage());
            }
            currentScope.addNode(node);
        }
        if(node.getType() instanceof ClassType)
            node.setType(currentScope.findType(node.getType().getTypeName()));
        if(node.getValue() == null) return;
        if(node.getValue().getExprType().getTypeName() != Name.getName("null")) {
            if (node.getType().getTypeName() != node.getValue().getExprType().getTypeName())
                errorHandle.addError(node.getLocation(),
                        node.getValue().getExprType().getTypeName() + " cannot be assigned to " +
                                node.getName().toString() + '(' + node.getType().getTypeName() + ')');
        }
        else {
            if(node.getType() instanceof BuiltInType)
                errorHandle.addError(node.getLocation(), "null cannot be assigned to built in type");
        }
    }

    @Override public void visit(FuncParamNode node) { }

    @Override
    public void visit(ExprNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(AndExprNode node) {
        if(node == null) return;
        visit(node.getLeft());
        visit(node.getRight());
        if(node.getLeft().getExprType().getTypeName() != Name.getName("bool")
                || node.getRight().getExprType().getTypeName() != Name.getName("bool"))
            errorHandle.addError(node.getLocation(), "condition must be of bool type");
        node.setExprType(node.getLeft().getExprType());
    }

    @Override
    public void visit(OrExprNode node) {
        if(node == null) return;
        visit(node.getLeft());
        visit(node.getRight());
        if(node.getLeft().getExprType().getTypeName() != Name.getName("bool")
                || node.getRight().getExprType().getTypeName() != Name.getName("bool"))
            errorHandle.addError(node.getLocation(), "condition must be of bool type");
        node.setExprType(node.getLeft().getExprType());
    }

    @Override
    public void visit(ArrayExprNode node) {
        visit(node.getArray());
        visit(node.getIndex());
        if(node.getIndex().getExprType().getTypeName() != Name.getName("int"))
            errorHandle.addError(node.getLocation(), "array index must be an integer"
                    + ", finding " + node.getIndex().getExprType().getTypeName().toString());
        if(!(node.getArray().getExprType() instanceof ArrayType))
            errorHandle.addError(node.getLocation(),
                    "'[]' can not be applied to non-array element" + node.getArray().getExprType().getTypeName().toString());
        if (((ArrayType)node.getArray().getExprType()).getDimension() > 1)
            node.setExprType(((ArrayType)node.getArray().getExprType()).getBasicType());
        else
            node.setExprType(currentScope.findType(((ArrayType)node.getArray().getExprType()).getBasicType().getTypeName()));
    }

    @Override
    public void visit(AssignExprNode node) {
        if(node == null) return;
        if(!node.getLeft().isLeftvalue())
            errorHandle.addError(node.getLocation(),
                    "assign operator can only be applied to a left value");
        visit(node.getLeft());
        visit(node.getRight());
        if(node.getRight().getExprType().getTypeName() != Name.getName("null")) {
            if(node.getLeft().getExprType() instanceof ArrayType) {
                if(!(node.getRight().getExprType() instanceof ArrayType))
                    errorHandle.addError(node.getLocation(), "type mismatch1");
                else if(node.getRight().getExprType().getTypeName() != node.getLeft().getExprType().getTypeName())
                    errorHandle.addError(node.getLocation(), "type mismatch2");
                else if(((ArrayType) node.getLeft().getExprType()).getDimension() != ((ArrayType) node.getRight().getExprType()).getDimension())
                    errorHandle.addError(node.getLocation(), "type mismatch3 " + ((ArrayType) node.getLeft().getExprType()).getDimension()
                                                                                      + ((ArrayType) node.getRight().getExprType()).getDimension());
            }
            else if (node.getRight().getExprType() instanceof ArrayType){
                errorHandle.addError(node.getLocation(), "cannot cast from var to array");
            }
            else if (node.getLeft().getExprType().getTypeName() != node.getRight().getExprType().getTypeName())
                errorHandle.addError(node.getLocation(), "cannot cast from "
                        + node.getLeft().getExprType().getTypeName().toString() + " to "
                        + node.getRight().getExprType().getTypeName().toString());
        }
        else {
            if(node.getLeft().getExprType() instanceof BuiltInType)
                errorHandle.addError(node.getLocation(), "null cannot be assigned to built in type");
        }
    }

    @Override
    public void visit(BinaryExprNode node) {
        if(node == null) return;
        visit(node.getL());
        visit(node.getR());
        if(node.getL().getExprType().getTypeName() != node.getR().getExprType().getTypeName()) {
            if(!((node.getOp() == BinaryOp.EQU || node.getOp() == BinaryOp.NEQ)&&
                    (node.getL().getExprType().getTypeName() == Name.getName("null")
                            || node.getR().getExprType().getTypeName() == Name.getName("null"))))
                errorHandle.addError(node.getLocation(), "cannot operate with type "
                        + node.getL().getExprType().getTypeName().toString() + " and type "
                        + node.getR().getExprType().getTypeName().toString());
        }
        if(BinaryOp.isArith(node.getOp()) && node.getL().getExprType().getTypeName() != Name.getName("int")) {
            if(!((node.getOp() == BinaryOp.ADD || BinaryOp.isCompare(node.getOp()))
                    && node.getL().getExprType().getTypeName() == Name.getName("string"))) {
                errorHandle.addError(node.getLocation(),
                        "type " + node.getL().getExprType().getTypeName().toString() + " cannot operate");
            }
        }
        if(BinaryOp.isCompare(node.getOp()))
            node.setExprType(new Type("bool", 1));
        else
            node.setExprType(node.getL().getExprType());
    }

    @Override public void visit(BoolConstNode node) { }

    @Override public void visit(IntConstNode node) { }

    @Override public void visit(StringConstNode node) { }

    @Override public void visit(NullConstNode node) { }

    @Override
    public void visit(CallExprNode node) {
        if(node == null)return;
        if(!currentScope.containsNode(node.getFuncName()))
            errorHandle.addError(node.getLocation(),
                    "function " + node.getFuncName().toString() + " have not been declared");
        FuncDeclNode function = (FuncDeclNode)currentScope.findNode(node.getFuncName());
        if(!function.isFunction())
            errorHandle.addError(node.getLocation(),
                    node.getFuncName().toString() + " is not a function");
        visit(node.getParameter());
        node.setExprType(function.getReturnType());
        node.setFunction(function);
        checkParameterMatch(function, node);
    }

    @Override
    public void visit(ExprListNode node) {
        if(node == null)return;
        for(ExprNode item : node.getExpresses())
            visit(item);
    }

    @Override
    public void visit(IdentifierExprNode node) {
        if(node == null) return;
        if(!currentScope.containsNode(node.getName()))
            errorHandle.addError(node.getLocation(),
                    node.getName().toString() + " have not been declared");
        VarDeclNode var = (VarDeclNode)currentScope.findNode(node.getName());
        node.setExprType(var.getType());
        if (node.getExprType() == null)
            System.out.println(1);
    }

    @Override
    public void visit(MemberExprNode node) {
        if(node == null) return;
        visit(node.getExpress());
        if(!(node.getExpress().getExprType() instanceof ClassType)) {
            if(node.getExpress().getExprType() instanceof ArrayType
                    && node.isFunctionCall()
                    && node.getFunctionCall().getFuncName() == Name.getName("size")) {
                visit(node.getFunctionCall());
                node.setExprType(new BuiltInType("int", 4));
            }
            else if (node.getExpress().getExprType().getTypeName() == Name.getName("string")
                    && node.isFunctionCall()
                    && isStringBuiltIn(node.getFunctionCall().getFuncName())) {
                visit(node.getFunctionCall());
                FuncDeclNode func = (FuncDeclNode)currentScope.findNode(node.getName());
                node.setExprType(func.getReturnType());
            }
            else {
                errorHandle.addError(node.getLocation(), "wrong usage of '.'");
            }
            return;
        }
        Scope scope = currentScope.findType(node.getExpress().getExprType().getTypeName()).getClassNode().getInternalScope();
        if(!scope.containsNode(node.getName())) {
            errorHandle.addError(node.getLocation(), node.getName().toString() + " have not been declared");
        }
        if(node.isFunctionCall()) {
            FuncDeclNode func = (FuncDeclNode)scope.findNode(node.getName());
            node.setExprType(func.getReturnType());
            visit(node.getFunctionCall().getParameter());
            checkParameterMatch(func, node.getFunctionCall());
            visit(node.getFunctionCall());
            node.getFunctionCall().setFunction(func);
        }
        else {
            node.setExprType(((VarDeclNode)scope.findNode(node.getName())).getType());
        }
    }

    @Override
    public void visit(NewExprNode node) {
        if(node == null) return;
        visit(node.getCreatorNode());
        node.setExprType(node.getCreatorNode().getExprType());
    }

    @Override
    public void visit(CreatorExprNode node) {
        if(node == null) return;
        for (ExprNode item: node.getExpresses())
            visit(item);
        if(!currentScope.containsType
                (node.getType()
                        .getTypeName())) {
            errorHandle.addError(node.getLocation(),
                    node.getType().getTypeName().toString() + " is not declared");
        }
        if(node.getSize() == 0) {
            node.setExprType(currentScope.findType(node.getType().getTypeName()));
        }
        else {
            node.setExprType(new ArrayType(node.getType(), node.getSize()));
        }
    }

    @Override
    public void visit(PrefixExprNode node) {
        if(node == null)return;
        if(UnaryOp.changeValue(node.getOption()) && !node.getExpr().isLeftvalue())
            errorHandle.addError(node.getLocation(),
                    node.getOption().toString() + " can only be applied to a left value");
        visit(node.getExpr());
        if(node.getOption() != UnaryOp.NOT && node.getExpr().getExprType().getTypeName() != Name.getName("int"))
            errorHandle.addError(node.getLocation(),
                    node.getOption().toString() + "can only be applied to integer");
        node.setExprType(node.getExpr().getExprType());
    }

    @Override
    public void visit(SuffixExprNode node) {
        if(node == null)return;
        visit(node.getExpr());
        if(!UnaryOp.isSuffix(node.getOption()))
            errorHandle.addError(node.getLocation(),
                    node.getOption().toString() + "is a prefix operator");
        if(UnaryOp.changeValue(node.getOption()) && !node.getExpr().isLeftvalue())
            errorHandle.addError(node.getLocation(),
                    node.getOption().toString() + " can only be applied to a left value");
        visit(node.getExpr());
        if(!(node.getExpr().getExprType().getTypeName() == Name.getName("int")))
            errorHandle.addError(node.getLocation(),
                    node.getOption().toString() + "can only be applied to integer");
        node.setExprType(node.getExpr().getExprType());
    }

    @Override
    public void visit(ThisExprNode node) {
        if(currentClass == null)
            errorHandle.addError(node.getLocation(), "'this' should be in a class");
        node.setExprType(currentClass.getType());
    }

    @Override
    public void visit(StmtNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        if(node == null) return;
        setCurrentScope(node.getScope());
        for(StmtNode item : node.getStatements())
            visit(item);
        exitCurrentScope();
    }

    @Override
    public void visit(BreakNode node) {
        if(!currentScope.isLoop())
            errorHandle.addError(node.getLocation(), "'break' must be in a loop");
    }

    @Override
    public void visit(ContinueNode node) {
        if(!currentScope.isLoop())
            errorHandle.addError(node.getLocation(), "'continue' must be in a loop");
    }

    @Override
    public void visit(ReturnNode node) {
        if(node == null) return;
        if(!currentScope.isFunction())
            errorHandle.addError(node.getLocation(), "'return' must be in a function");
        if(node.getExpress() == null) return;
        if(currentFunction.isConstructor() && node.getExpress() != null) {
            errorHandle.addError(node.getLocation(), "cannot return any value in constructors");
        }
        visit(node.getExpress());
        if(!Type.equal(currentFunction.getReturnType(), node.getExpress().getExprType()))
            errorHandle.addError(node.getLocation(),
                    "function " + currentFunction.getName().toString() + " should not return a "
                            +node.getExpress().getExprType().getTypeName().toString());
    }

    @Override
    public void visit(ForNode node) {
        if(node == null) return;
        setCurrentScope(node.getInternalScope());
        visit(node.getBeginCondition());
        visit(node.getEndCondition());
        if(node.getEndCondition() != null) {
            if (node.getEndCondition().getExprType().getTypeName() != Name.getName("bool"))
                errorHandle.addError(node.getLocation(),
                        "end condition should be of bool type");
        }
        visit(node.getUpdate());
        visit(node.getBlock());
        node.setInternalScope(currentScope);
        exitCurrentScope();
    }

    @Override
    public void visit(IfNode node) {
        if(node == null) return;
        setCurrentScope(node.getInternalScope());
        visit(node.getCondition());
        if(node.getCondition().getExprType().getTypeName() != Name.getName("bool"))
            errorHandle.addError(node.getLocation(),
                    "conditions in if statement must be of bool type");
        visit(node.getState());
        if(node.getElsestate() != null)
            visit(node.getElsestate());
        node.setInternalScope(currentScope);
        exitCurrentScope();
    }

    @Override
    public void visit(WhileNode node) {
        if(node == null) return;
        setCurrentScope(node.getInternalScope());
        visit(node.getCondition());
        if(node.getCondition().getExprType().getTypeName() != Name.getName("bool"))
            errorHandle.addError(node.getLocation(),
                    "conditions in while statement must be of bool type");
        visit(node.getBlock());
        node.setInternalScope(currentScope);
        exitCurrentScope();
    }

    @Override
    public void visit(ExprStatNode node) {
        if(node == null) return;
        visit(node.getExpress());
    }

    @Override
    public void visit(Type type) {
        if(type instanceof ArrayType) {
            if(!currentScope.containsType(((ArrayType) type).getBaseType().getTypeName())) {
                throw new RuntimeException("type " + ((ArrayType) type).getBaseType().getTypeName().toString()
                        + " have not been declared");
            }
        }
        if(!currentScope.containsType(type.getTypeName()))
            throw new RuntimeException("type " + type.getTypeName().toString() + " have not been declared");
    }



    private boolean isStringBuiltIn(Name name) {
        return (name == Name.getName("length")
                || name == Name.getName("substring")
                || name == Name.getName("parseInt")
                || name == Name.getName("ord"));
    }

    private void setCurrentScope(Scope _currentScope) {
        currentScope = _currentScope;
        scopeStack.push(_currentScope);
    }

    private void exitCurrentScope() {
        scopeStack.pop();
        currentScope = scopeStack.peek();
    }

    private void checkParameterMatch(FuncDeclNode decl, CallExprNode call) {
        if(decl.getParameter().size() != call.getParameter().getExpresses().size())
            errorHandle.addError(call.getLocation(),
                    "wrong number of parameters");
        List<FuncParamNode> formal_params = decl.getParameter();
        List<ExprNode> actual_params = call.getParameter().getExpresses();
        for(int i = 1; i < decl.getParameter().size(); ++i) {
            if(!Type.equal(formal_params.get(i).getType(), actual_params.get(i).getExprType()))
                errorHandle.addError(call.getLocation(),
                        "required type " + formal_params.get(i).getType().getTypeName()
                                + " finding " + actual_params.get(i).getExprType().getTypeName());
        }
    }

}
