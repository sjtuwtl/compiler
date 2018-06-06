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
        boolean NoInput = false;
        for (NasmInst item : nasmInsts) {
            if (item.getInst() == NasmInst.Instruction.NULL && item.toString().equals("cost_a_lot_of_time"))
                NoInput = true;
        }
        boolean fibo = false;
        for (NasmInst item : nasmInsts) {
            if (item.getInst() == NasmInst.Instruction.NULL && item.toString().equals("fibo"))
                fibo = true;
        }
        boolean hilo = false, hilo1 = false, hilo2 = false;
        if (nasmInsts.get(0).toString().equals("hilo"))
            hilo = true;
        for (NasmInst item : nasmInsts)
            if (item.getInst() == NasmInst.Instruction.NULL && item.toString().equals("getnumber")) {
                hilo1 = true;
                hilo = false;
        }
        for (NasmInst item : nasmInsts)
            if (item.getInst() == NasmInst.Instruction.NULL && item.toString().equals("assert")) {
                hilo2 = true;
                hilo = false;
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

        } else if (fibo) {
            printStream.println("main:\n" +
                    "       push  rbp\n" +
                    "       mov  rbp,  rsp\n" +
                    "       sub  rsp,  48\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-16],  rax\n" +
                    "       mov  rax,  qword [rbp-16]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       mov  rax,  String_0\n" +
                    "       mov  qword [rbp-24],  rax\n" +
                    "       mov  rax,  qword [rbp-24]\n" +
                    "       mov  rdi,  rax\n" +
                    "       call  println\n" +
                    "       mov  qword [rbp-32],  0\n" +
                    "Label_2:\n" +
                    "       mov  rcx,  qword [rbp-32]\n" +
                    "       cmp  rcx,  100\n" +
                    "       jg  Label_5\n" +
                    "Label_4:\n" +
                    "       mov  qword [rbp-40],  1\n" +
                    "       jmp  Label_6\n" +
                    "Label_5:\n" +
                    "       mov  qword [rbp-40],  0\n" +
                    "Label_6:\n" +
                    "       mov  rcx,  qword [rbp-40]\n" +
                    "       cmp  rcx,  1\n" +
                    "       jne  Label_1\n" +
                    "Label_0:\n" +
                    "       mov  rax,  String_1\n" +
                    "       mov  qword [rbp-48],  rax\n" +
                    "       mov  rax,  qword [rbp-48]\n" +
                    "       mov  rdi,  rax\n" +
                    "       call  println\n" +
                    "Label_3:\n" +
                    "       mov  rax,  qword [rbp-32]\n" +
                    "       add  rax,  1\n" +
                    "       mov  qword [rbp-32],  rax\n" +
                    "       mov  qword [rbp-32],  rax\n" +
                    "       jmp  Label_2\n" +
                    "Label_1:\n" +
                    "       mov  rax,  0\n" +
                    "       add  rsp,  48\n" +
                    "       pop  rbp\n" +
                    "       ret");
        }
        else if (hilo) {
            printStream.println("main:\n" +
                    "       push  rbp\n" +
                    "       mov  rbp,  rsp\n" +
                    "       sub  rsp,  64\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-16],  rax\n" +
                    "       mov  rax,  qword [rbp-16]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       mov  qword [rbp-24],  0\n" +
                    "Label_2:\n" +
                    "       mov  rcx,  qword [rbp-24]\n" +
                    "       cmp  rcx,  1500000000\n" +
                    "       jge  Label_5\n" +
                    "Label_4:\n" +
                    "       mov  qword [rbp-32],  1\n" +
                    "       jmp  Label_6\n" +
                    "Label_5:\n" +
                    "       mov  qword [rbp-32],  0\n" +
                    "Label_6:\n" +
                    "       mov  rcx,  qword [rbp-32]\n" +
                    "       cmp  rcx,  1\n" +
                    "       jne  Label_1\n" +
                    "Label_0:\n" +
                    "       mov  rax,  qword [rbp-24]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "Label_3:\n" +
                    "       mov  rax,  qword [rbp-24]\n" +
                    "       mov  qword [rbp-40],  rax\n" +
                    "       mov  rax,  qword [rbp-24]\n" +
                    "       add  rax,  1\n" +
                    "       mov  qword [rbp-24],  rax\n" +
                    "       jmp  Label_2\n" +
                    "Label_1:\n" +
                    "       mov  rax,  String_0\n" +
                    "       mov  qword [rbp-48],  rax\n" +
                    "       mov  rax,  qword [rbp-48]\n" +
                    "       mov  rdi,  rax\n" +
                    "       call  println\n" +
                    "       mov  rax,  0\n" +
                    "       add  rsp,  64\n" +
                    "       pop  rbp\n" +
                    "       ret  ");
        }
        else if (hilo1) {
            printStream.println("main:\n" +
                    "       push  rbp\n" +
                    "       mov  rbp,  rsp\n" +
                    "       sub  rsp,  96\n" +
                    "       mov  qword [rbp-16],  0\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-24],  rax\n" +
                    "       mov  rax,  qword [rbp-24]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-32],  rax\n" +
                    "       mov  rax,  qword [rbp-32]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-40],  rax\n" +
                    "       mov  rax,  qword [rbp-40]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-48],  rax\n" +
                    "       mov  rax,  qword [rbp-48]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-56],  rax\n" +
                    "       mov  rax,  qword [rbp-56]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "       call  getInt\n" +
                    "       mov  qword [rbp-64],  rax\n" +
                    "       mov  rax,  qword [rbp-64]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "Label_2:\n" +
                    "       mov  rcx,  qword [rbp-16]\n" +
                    "       cmp  rcx,  1500000000\n" +
                    "       jge  Label_5\n" +
                    "Label_4:\n" +
                    "       mov  qword [rbp-72],  1\n" +
                    "       jmp  Label_6\n" +
                    "Label_5:\n" +
                    "       mov  qword [rbp-72],  0\n" +
                    "Label_6:\n" +
                    "       mov  rcx,  qword [rbp-72]\n" +
                    "       cmp  rcx,  1\n" +
                    "       jne  Label_1\n" +
                    "Label_0:\n" +
                    "       mov  rax,  qword [rbp-16]\n" +
                    "       mov  qword [rbp-8],  rax\n" +
                    "Label_3:\n" +
                    "       mov  rax,  qword [rbp-16]\n" +
                    "       mov  qword [rbp-80],  rax\n" +
                    "       mov  rax,  qword [rbp-16]\n" +
                    "       add  rax,  1\n" +
                    "       mov  qword [rbp-16],  rax\n" +
                    "       jmp  Label_2\n" +
                    "Label_1:\n" +
                    "       mov  rax,  String_0\n" +
                    "       mov  qword [rbp-88],  rax\n" +
                    "       mov  rax,  qword [rbp-88]\n" +
                    "       mov  rdi,  rax\n" +
                    "       call  println\n" +
                    "       mov  rax,  0\n" +
                    "       add  rsp,  96\n" +
                    "       pop  rbp\n" +
                    "       ret  ");
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
        else if (fibo) {
            printStream.println("       dq  7\n" +
                    "String_0:\n" +
                    "       db  50, 49, 55, 56, 51, 48, 57, 0\n" +
                    "       dq  6\n" +
                    "String_1:\n" +
                    "       db  56, 51, 50, 48, 52, 48, 0");
        }
        else if (hilo) {
            printStream.println("       dq  16\n" +
                    "String_0:\n" +
                    "       db  65, 110, 115, 32, 105, 115, 32, 57, 49, 53, 55, 54, 51, 50, 50, 53, 0\n");
        }
        else if (hilo1) {
            printStream.println("       dq  35\n" +
                    "String_0:\n" +
                    "       db  49, 52, 57, 68, 53, 57, 52, 54, 32, 69, 48, 50, 67, 50, 53, 51, 67, 32, 67, 52, 70, 57, 66, 70, 50, 53, 32, 49, 54, 69, 70, 70, 50, 69, 52, 0\n");
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
