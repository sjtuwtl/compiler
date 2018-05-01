package wtlcompiler.AST;

import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.Type.*;
import wtlcompiler.AST.tool.ASTVisitor;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class ASTPrinter implements ASTVisitor {
    private StringBuilder indent = new StringBuilder();
    private PrintStream printStream;

    private void Tab() {
        indent.append("  ");
    }

    private void BackSpace() {
        indent.delete(indent.length() - 2, indent.length());
    }

    public void PrintAST(ProgNode program, FileOutputStream outputStream) {
        printStream = new PrintStream(outputStream);
        visit(program);
    }

    @Override
    public void visit(ProgNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        for(DeclNode item : node.getDeclares())
            visit(item);
        BackSpace();
    }

    @Override
    public void visit(DeclNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + "*ClassDeclaration: " + node.getName());
        Tab();
        for(FuncDeclNode item : node.getMemberFunction())
            visit(item);
        for(VarDeclNode item : node.getMemberVarible())
            visit(item);
        BackSpace();
    }

    @Override
    public void visit(FuncDeclNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + "*FunctionDeclaration: " + node.getName());
        Tab();
        if(node.getFunction().getReturnType() != null) visit(node.getFunction().getReturnType());
        if(node.getFunction().getParameter() != null)
        {
            for (FuncParamNode item : node.getFunction().getParameter())
                visit(item);
        }
        visit(node.getBlock());
        BackSpace();
    }

    @Override
    public void visit(VarDeclNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + "*VariableDeclaration: " + node.getName());
        Tab();
        visit(node.getVar().getType());
        if(node.getValue() != null)
            visit(node.getValue());
        BackSpace();
    }

    @Override
    public void visit(FuncParamNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName()
                + " Type: " + node.getParameter().getType().getTypeName() + "    Name: " + node.getParameter().getName());
    }

    @Override
    public void visit(CreatorExprNode node) { }

    @Override
    public void visit(ExprNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(AndExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getLeft());
        visit(node.getRight());
        BackSpace();
    }

    @Override
    public void visit(ArrayExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getArray());
        visit(node.getIndex());
        BackSpace();
    }

    @Override
    public void visit(AssignExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getLeft());
        visit(node.getRight());
        BackSpace();
    }

    @Override
    public void visit(BinaryExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  Op: " + node.getOp().toString(node.getOp()));
        Tab();
        visit(node.getL());
        visit(node.getR());
        BackSpace();
    }

    @Override
    public void visit(BoolConstNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  value = " + node.getBool());
    }

    @Override
    public void visit(CallExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  Function Name: " + node.getFuncName());
        Tab();
        visit(node.getParameter());
        BackSpace();
    }


    @Override
    public void visit(ExprListNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        for(ExprNode item : node.getExpresses())
        {
            visit(item);
        }
        BackSpace();
    }

    @Override
    public void visit(IdentifierExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  ID name: " + node.getName());
    }

    @Override
    public void visit(ExprStatNode node)
    {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getExpress());
        BackSpace();
    }

    @Override
    public void visit(IntConstNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  value = " + node.getInteger());
    }

    @Override
    public void visit(MemberExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getExpress());
        BackSpace();
    }

    @Override
    public void visit(NewExprNode node) { }

    @Override
    public void visit(NullConstNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
    }

    @Override
    public void visit(OrExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getLeft());
        visit(node.getRight());
        BackSpace();
    }

    @Override
    public void visit(PrefixExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  Op: " + node.getOption().toString(node.getOption()));
        Tab();
        visit(node.getExpr());
        BackSpace();
    }

    @Override
    public void visit(StringConstNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  value = " + node.getStr());
    }

    @Override
    public void visit(SuffixExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + "  Op: " + node.getOption().toString(node.getOption()));
        Tab();
        visit(node.getExpr());
        BackSpace();
    }

    @Override
    public void visit(ThisExprNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + node.getName());
    }

    @Override
    public void visit(StmtNode node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        for(StmtNode item : node.getStatements())
        {
            visit(item);
        }
        BackSpace();
    }

    @Override
    public void visit(BreakNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
    }

    @Override
    public void visit(ContinueNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
    }

    @Override
    public void visit(ForNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getBeginCondition());
        visit(node.getEndCondition());
        visit(node.getUpdate());
        visit(node.getBlock());
        BackSpace();
    }

    @Override
    public void visit(IfNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getCondition());
        visit(node.getState());
        if(node.getElsestate() != null) visit(node.getElsestate());
        BackSpace();
    }

    @Override
    public void visit(ReturnNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
    }

    @Override
    public void visit(WhileNode node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName());
        Tab();
        visit(node.getCondition());
        visit(node.getBlock());
        BackSpace();
    }

    @Override
    public void visit(Type node) {
        if(node == null) return;
        printStream.println(indent.toString() + node.getClass().getSimpleName() + ": " + node.getTypeName());
    }

}
