package wtlcompiler.IR.IRBase;

import wtlcompiler.AST.node.DeclNode.FuncDeclNode;
import wtlcompiler.IR.Value.VitualRegister;
import wtlcompiler.utility.Name;

import java.util.Vector;

public class FunctionScope {
    private Vector<VitualRegister> registerMap = new Vector<VitualRegister>(1);
    private int curNum = 0;
    private Name name;
    private FuncDeclNode funcDeclNode;
    private int usedSlotNum = 0;

    public FunctionScope(Name name) {
        this.name = name;
    }

    public VitualRegister getRegister() {
        VitualRegister register = new VitualRegister(Name.getName("%r" + String.valueOf(curNum++)));
        registerMap.addElement(register);
        return register;
    }

    public VitualRegister getRegister(int number) {
        return registerMap.elementAt(number);
    }

    public int getCurNum() {
        return curNum;
    }

    public FuncDeclNode getFuncDeclNode() {
        return funcDeclNode;
    }

    public int getUsedSlotNum() {
        return usedSlotNum;
    }

    public void setFuncDeclNode(FuncDeclNode funcDeclNode) {
        this.funcDeclNode = funcDeclNode;
    }

    public Name getName() {
        return name;
    }

    public void incSlotNum() {
        ++usedSlotNum;
    }
}
