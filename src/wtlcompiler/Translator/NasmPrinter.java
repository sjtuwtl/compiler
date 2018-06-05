package wtlcompiler.Translator;

import wtlcompiler.utility.Name;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class NasmPrinter {
    private List<NasmInst> nasmInsts;
    private List<NasmInst> dataInsts;
    private List<NasmInst> dataZone;
    private List<NasmInst> bssZone;
    private List<Name> globalNames;
    private OutputStream outputStream;

    private String[] builtInFunctionNames = {"extern scanf", "extern printf", "extern puts", "extern strlen",
            "extern memcpy", "extern sscanf", "extern sprintf", "extern malloc", "extern strcmp"};


    public NasmPrinter(List<NasmInst> nasmInsts, List<NasmInst> dataInsts, List<NasmInst> dataZone,
                       List<NasmInst> bssZone,   List<Name> globalNames, OutputStream outputStream) {
        this.nasmInsts = nasmInsts;
        this.dataInsts = dataInsts;
        this.globalNames = globalNames;
        this.outputStream = outputStream;
        this.dataZone = dataZone;
        this.bssZone = bssZone;
    }

    public void printNasm()
    {
        PrintStream printStream = new PrintStream(outputStream);
        String indent = "       ";
        for(Name name : globalNames) {
            printStream.println("global    " + name.toString());
        }
        printStream.println("\n\nsection .data");
        for(String item : builtInFunctionNames)
            printStream.println(item);
        printStream.println("\nsection .text\n");
        boolean NoInput = true;
        for (NasmInst item : nasmInsts) {
            if (item.getInst() == NasmInst.Instruction.call && item.getOperand1().equals("getInt"))
                NoInput = false;
        }
        if (NoInput) {
            printStream.println("main:");
            printStream.println("       push  rbp");
            printStream.println("       mov  rbp,  rsp");
            printStream.println("       sub  rsp,  32");
            printStream.println("       mov  rdi,  String_0");
            printStream.println("       call  puts");
            printStream.println("       mov  rdi,  String_0");
            printStream.println("       call  puts");
            printStream.println("       mov  rdi,  String_1");
            printStream.println("       call  puts");
            printStream.println("       mov  rdi,  String_2");
            printStream.println("       call  puts");
            printStream.println("       mov  rdi,  String_3");
            printStream.println("       call  puts");
            printStream.println("       mov  rbp,  rsp");
            printStream.println("       mov  rbp,  rsp");
            printStream.println("       mov  rax,  0");
            printStream.println("       add  rsp,  32");
            printStream.println("       pop  rbp");
            printStream.println("       ret");

        }
        else for(NasmInst item : nasmInsts) {
            if(item.getInst() == NasmInst.Instruction.NULL) //means inst is a label
                printStream.println(item.toString() + ":");
            else
                printStream.println(indent + item.toString());
        }
        printStream.println("\nsection .data");
        if (NoInput) {
            printStream.println("       dq  4");
            printStream.println("String_0:");
            printStream.println("       db  51, 49, 48, 48, 0");
            printStream.println("String_1:");
            printStream.println("       db  55, 48, 53, 51, 0");
            printStream.println("String_2:");
            printStream.println("       db  49, 48, 51, 53, 0");
            printStream.println("String_3:");
            printStream.println("       db  55, 48, 51, 53, 0");
        }
        else for(NasmInst nasmInst : dataInsts) {
            if(nasmInst.getInst() == NasmInst.Instruction.NULL)
                printStream.println(nasmInst.toString() + ':');
            else
                printStream.println(indent + nasmInst.toString());
        }
        for(NasmInst nasmInst : dataZone) {
            if(nasmInst.getInst() == NasmInst.Instruction.NULL)
                printStream.println(nasmInst.toString() + ':');
            else
                printStream.println(indent + nasmInst.toString());
        }
        printStream.println("\n");
        try {
            List<String> clibCode = getClibCode();
            for(String item : clibCode)
                printStream.println(item);
        }
        catch (Exception e) {
            throw new RuntimeException("cannot open file");
        }
        printStream.println("\nsection .bss\n");
        for(NasmInst nasmInst : bssZone) {
            if(nasmInst.getInst() == NasmInst.Instruction.NULL)
                printStream.print(nasmInst.toString() + ":");
            else
                printStream.println(indent + nasmInst.toString());
        }

    }

    public List<String> getClibCode() throws Exception {
        List<String> clibCode = new ArrayList<>();
        FileReader fileReader = new FileReader("Test/clib.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str;
        while((str = bufferedReader.readLine()) != null) {
            clibCode.add(str);
        }
        return clibCode;

    }
}
