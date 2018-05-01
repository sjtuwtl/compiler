import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import wtlcompiler.AST.ASTBuilder;
import wtlcompiler.AST.ASTPrinter;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.utility.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.tree.*;
import Parser.Parser.*;
import Parser.*;

public class Main {
    public static void checkSemantic(ProgNode progNode) {
        Scope topLevelScope = new Scope(true);
        ErrorHandle handle = new ErrorHandle();
        topLevelScope.Initialize();
        ScopeColletor scopeColletor = new ScopeColletor(topLevelScope, handle);
        scopeColletor.process(progNode);
        FunctionCollector functionCollector = new FunctionCollector(topLevelScope, handle);
        functionCollector.process(progNode);
        Semantic semanticChecker = new Semantic(topLevelScope, handle);
        semanticChecker.process(progNode);
    }

    public static void printAST(ProgNode program) throws Exception {
        FileOutputStream outputStream = new FileOutputStream("Test/TestSemantic/test_result.txt");
        ASTPrinter printer = new ASTPrinter();
        printer.PrintAST(program, outputStream);
    }

    public static void main(String[] args) throws Exception {
 //     InputStream is = System.in;
//       InputStream is = new FileInputStream("Test/TestSemantic/text.txt");
         InputStream is = new FileInputStream("program.txt");
        ANTLRInputStream input = new ANTLRInputStream(is);
        tryLexer lexer = new tryLexer(input);
//        lexer.addErrorListener(tryErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tryParser parser = new tryParser(tokens);
//        parser.addErrorListener(tryErrorListener.INSTANCE);
        ParseTree tree = parser.definition();

        ParseTreeWalker walker = new ParseTreeWalker();
        ASTBuilder constructor = new ASTBuilder();
        walker.walk(constructor, tree);

//        printAST(constructor.getProgram());

        checkSemantic(constructor.getProgram());
    }

}
