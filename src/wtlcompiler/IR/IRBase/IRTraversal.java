package wtlcompiler.IR.IRBase;

import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.Type.*;
import wtlcompiler.IR.Function;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.IR.Value.*;
import wtlcompiler.IR.IRType.*;

public interface IRTraversal {
    void visit(ProgNode node);

    IRInstruction visit(DeclNode node);
    IRInstruction visit(ClassDeclNode node);
    Function visit(FuncDeclNode node);
    Address visit(VarDeclNode node);
    IRType visit(FuncParamNode node);

    IntegerValue visit(ExprNode node);
    IntegerValue visit(AndExprNode node);
    IntegerValue visit(ArrayExprNode node);
    IntegerValue visit(AssignExprNode node);
    IntegerValue visit(BinaryExprNode node);
    IntegerValue visit(BoolConstNode node);
    IntegerValue visit(CallExprNode node);
    IntegerValue visit(ExprListNode node);
    IntegerValue visit(IdentifierExprNode node);
    Immediate    visit(IntConstNode node);
    IntegerValue visit(MemberExprNode node);
    IntegerValue visit(NewExprNode node);
    IntegerValue visit(CreatorExprNode node);
    IntegerValue visit(NullConstNode node);
    IntegerValue visit(OrExprNode node);
    IntegerValue visit(PrefixExprNode node);
    IntegerValue visit(StringConstNode node);
    IntegerValue visit(SuffixExprNode node);
    IntegerValue visit(ThisExprNode node);

    IRInstruction visit(StmtNode node);
    IRInstruction visit(BlockNode node);
    IRInstruction visit(BreakNode node);
    IRInstruction visit(ContinueNode node);
    IRInstruction visit(ForNode node);
    IRInstruction visit(IfNode node);
    IRInstruction visit(ReturnNode node);
    IRInstruction visit(WhileNode node);
    IRInstruction visit(ExprStatNode node);

    IRType visit(Type type);
}
