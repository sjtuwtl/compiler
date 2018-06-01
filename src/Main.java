import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import wtlcompiler.AST.ASTBuilder;
import wtlcompiler.AST.ASTPrinter;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.IR.IRBase.DataSection;
import wtlcompiler.IR.IRBase.IRConstructor;
import wtlcompiler.IR.IRBase.IRPrinter;
import wtlcompiler.IR.IRInstruction;
import wtlcompiler.IR.IRType.Class;
import wtlcompiler.Optimizer.Allocator;
import wtlcompiler.Translator.NasmPrinter;
import wtlcompiler.Translator.Translator;
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
        FileOutputStream outputStream = new FileOutputStream("Test/ASTtest_result.txt");
        ASTPrinter printer = new ASTPrinter();
        printer.PrintAST(program, outputStream);
    }

    public static ProgNode buildAST(InputStream is) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(is);
        tryLexer lexer = new tryLexer(input);
//        lexer.addErrorListener(tryErrorListener.INSTANCE);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tryParser parser = new tryParser(tokens);
//        parser.addErrorListener(tryErrorListener.INSTANCE);
        ParseTree tree = parser.definition();

        ParseTreeWalker walker = new ParseTreeWalker();
        ASTBuilder builder = new ASTBuilder();
        walker.walk(builder, tree);

        return builder.getProgram();
    }

    public static IRConstructor constructIR(ProgNode program)
    {
        IRConstructor irConstructor = new IRConstructor(program);
        irConstructor.BuildIR();
        return irConstructor;
    }

    public static void printIR(IRConstructor irConstructor) throws Exception
    {
        FileOutputStream outputStream = new FileOutputStream("Test/IRtest_result.txt");
        IRInstruction entry = irConstructor.getEntry();
        List<Class> typeList = irConstructor.getTypes();
        DataSection dataSection = irConstructor.getDataSection();
        IRPrinter irPrinter = new IRPrinter(entry, irConstructor.getInitializeEntry(), outputStream, typeList ,dataSection);
        irPrinter.printIR();
    }

    public static void translate(IRConstructor irConstructor, OutputStream outputStream) {
        Translator translator = new Translator(irConstructor.getEntry(), irConstructor.getInitializeEntry(),
                irConstructor.getDataSection(), irConstructor.getDataZone(), irConstructor.getBssZone());
        translator.process();
        NasmPrinter nasmPrinter = new NasmPrinter(translator.getNasmInsts(), translator.getDataInsts(),
                translator.getDataZoneInsts(), translator.getBssZoneInsts(),
                irConstructor.getGlobalName(), outputStream);
        nasmPrinter.printNasm();
    }
    public static void optimizeIR(IRConstructor irConstructor) {
        Allocator allocator = new Allocator(irConstructor.getEntry(), irConstructor.getInitializeEntry());
        allocator.process();
    }

    public static void main(String[] args) throws Exception {
//        InputStream is = System.in;
//      InputStream is = new FileInputStream("Test/text.txt");
        OutputStream out = System.out;
         InputStream is = new FileInputStream("program.txt");
        ProgNode program = buildAST(is);


//        printAST(program);

        checkSemantic(program);
        IRConstructor constructor = constructIR(program);

//        printIR(constructor);
        optimizeIR(constructor);
        translate(constructor, out);
    }

}
