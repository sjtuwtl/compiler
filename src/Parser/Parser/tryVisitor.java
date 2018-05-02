// Generated from D:/compiler/src/Parser\try.g4 by ANTLR 4.7
package Parser.Parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link tryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface tryVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link tryParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(tryParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#classname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassname(tryParser.ClassnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#classdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassdef(tryParser.ClassdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#funname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunname(tryParser.FunnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#fundef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFundef(tryParser.FundefContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#varname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarname(tryParser.VarnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#varsdef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarsdef(tryParser.VarsdefContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#vardef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardef(tryParser.VardefContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParams(tryParser.ParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(tryParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#typename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypename(tryParser.TypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#basetype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasetype(tryParser.BasetypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#arraytype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraytype(tryParser.ArraytypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(tryParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(tryParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(tryParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varDeclStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclStmt(tryParser.VarDeclStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(tryParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmt(tryParser.ForStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whilStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhilStmt(tryParser.WhilStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmt(tryParser.ReturnStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStmt(tryParser.BreakStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStmt(tryParser.ContinueStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStmt(tryParser.EmptyStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(tryParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#exprs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprs(tryParser.ExprsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpr(tryParser.NewExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolConstExpr(tryParser.BoolConstExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExpr(tryParser.ThisExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpr(tryParser.NullExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExpr(tryParser.ArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberExpr(tryParser.MemberExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixExpr(tryParser.SuffixExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(tryParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(tryParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntConstExpr(tryParser.IntConstExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExpr(tryParser.SubExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixExpr(tryParser.PrefixExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstExpr(tryParser.StringConstExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExpr(tryParser.CallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExpr(tryParser.AssignExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpr(tryParser.IdExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(tryParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code wrongCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrongCreator(tryParser.WrongCreatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreator(tryParser.ArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonArrayCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonArrayCreator(tryParser.NonArrayCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link tryParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(tryParser.FunctionCallContext ctx);
}