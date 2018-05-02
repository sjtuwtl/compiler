// Generated from D:/compiler/src/Parser\try.g4 by ANTLR 4.7
package Parser.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link tryParser}.
 */
public interface tryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link tryParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(tryParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(tryParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#classname}.
	 * @param ctx the parse tree
	 */
	void enterClassname(tryParser.ClassnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#classname}.
	 * @param ctx the parse tree
	 */
	void exitClassname(tryParser.ClassnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#classdef}.
	 * @param ctx the parse tree
	 */
	void enterClassdef(tryParser.ClassdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#classdef}.
	 * @param ctx the parse tree
	 */
	void exitClassdef(tryParser.ClassdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#funname}.
	 * @param ctx the parse tree
	 */
	void enterFunname(tryParser.FunnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#funname}.
	 * @param ctx the parse tree
	 */
	void exitFunname(tryParser.FunnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#fundef}.
	 * @param ctx the parse tree
	 */
	void enterFundef(tryParser.FundefContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#fundef}.
	 * @param ctx the parse tree
	 */
	void exitFundef(tryParser.FundefContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#varname}.
	 * @param ctx the parse tree
	 */
	void enterVarname(tryParser.VarnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#varname}.
	 * @param ctx the parse tree
	 */
	void exitVarname(tryParser.VarnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#varsdef}.
	 * @param ctx the parse tree
	 */
	void enterVarsdef(tryParser.VarsdefContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#varsdef}.
	 * @param ctx the parse tree
	 */
	void exitVarsdef(tryParser.VarsdefContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#vardef}.
	 * @param ctx the parse tree
	 */
	void enterVardef(tryParser.VardefContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#vardef}.
	 * @param ctx the parse tree
	 */
	void exitVardef(tryParser.VardefContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#params}.
	 * @param ctx the parse tree
	 */
	void enterParams(tryParser.ParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#params}.
	 * @param ctx the parse tree
	 */
	void exitParams(tryParser.ParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(tryParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(tryParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#typename}.
	 * @param ctx the parse tree
	 */
	void enterTypename(tryParser.TypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#typename}.
	 * @param ctx the parse tree
	 */
	void exitTypename(tryParser.TypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#basetype}.
	 * @param ctx the parse tree
	 */
	void enterBasetype(tryParser.BasetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#basetype}.
	 * @param ctx the parse tree
	 */
	void exitBasetype(tryParser.BasetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#arraytype}.
	 * @param ctx the parse tree
	 */
	void enterArraytype(tryParser.ArraytypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#arraytype}.
	 * @param ctx the parse tree
	 */
	void exitArraytype(tryParser.ArraytypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(tryParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(tryParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(tryParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(tryParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(tryParser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(tryParser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varDeclStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclStmt(tryParser.VarDeclStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varDeclStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclStmt(tryParser.VarDeclStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(tryParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(tryParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(tryParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(tryParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whilStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhilStmt(tryParser.WhilStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whilStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhilStmt(tryParser.WhilStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(tryParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(tryParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(tryParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(tryParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(tryParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(tryParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStmt(tryParser.EmptyStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStmt(tryParser.EmptyStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(tryParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link tryParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(tryParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#exprs}.
	 * @param ctx the parse tree
	 */
	void enterExprs(tryParser.ExprsContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#exprs}.
	 * @param ctx the parse tree
	 */
	void exitExprs(tryParser.ExprsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNewExpr(tryParser.NewExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNewExpr(tryParser.NewExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolConstExpr(tryParser.BoolConstExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolConstExpr(tryParser.BoolConstExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterThisExpr(tryParser.ThisExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitThisExpr(tryParser.ThisExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNullExpr(tryParser.NullExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNullExpr(tryParser.NullExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrayExpr(tryParser.ArrayExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrayExpr(tryParser.ArrayExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(tryParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(tryParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSuffixExpr(tryParser.SuffixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSuffixExpr(tryParser.SuffixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(tryParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(tryParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(tryParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(tryParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntConstExpr(tryParser.IntConstExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntConstExpr(tryParser.IntConstExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubExpr(tryParser.SubExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubExpr(tryParser.SubExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExpr(tryParser.PrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExpr(tryParser.PrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStringConstExpr(tryParser.StringConstExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringConstExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStringConstExpr(tryParser.StringConstExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(tryParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(tryParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(tryParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(tryParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdExpr(tryParser.IdExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdExpr(tryParser.IdExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(tryParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link tryParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(tryParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code wrongCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterWrongCreator(tryParser.WrongCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code wrongCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitWrongCreator(tryParser.WrongCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreator(tryParser.ArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreator(tryParser.ArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonArrayCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayCreator(tryParser.NonArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonArrayCreator}
	 * labeled alternative in {@link tryParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayCreator(tryParser.NonArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link tryParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(tryParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link tryParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(tryParser.FunctionCallContext ctx);
}