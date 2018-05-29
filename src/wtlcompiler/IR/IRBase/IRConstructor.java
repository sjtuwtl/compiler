package wtlcompiler.IR.IRBase;

import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.Immediate;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.VitualRegister;
import wtlcompiler.utility.Name;
import wtlcompiler.utility.BinaryOp;
import wtlcompiler.Type.*;
import wtlcompiler.IR.*;
import wtlcompiler.IR.IRType.Class;
import wtlcompiler.IR.IRType.IRType;
import wtlcompiler.IR.IRType.BuiltIn;
import wtlcompiler.IR.IRType.Array;
import wtlcompiler.IR.Label;
import wtlcompiler.utility.UnaryOp;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IRConstructor implements IRTraversal {
    private IRInstruction curInst;
    private Label curLab;
    private IRScope curIRScope;
    private FunctionScope curFunc;
    private ClassDeclNode curClass;

    private Label nextLabel;
    private Address addressForCond;
    private boolean isVarForFond;
    private Stack<Label> nextLabelStack = new Stack<>();
    private IRInstruction entry;
    private ProgNode program;
    private List<Class> types = new ArrayList<>();
    private DataSection dataSection = new DataSection();
    private List<Name> globalName = new ArrayList<>();

    public IRConstructor(ProgNode progNode) {
        program = progNode;
        curFunc = new FunctionScope(Name.getName("GLOBAL"));
        curLab = new Label("GLOBAL");
        setNextLabel(new Label("GLOBAL"));
    }

    public IRInstruction getEntry() {
        return entry;
    }

    public List<Class> getTypes() {
        return types;
    }

    public DataSection getDataSection() {
        return dataSection;
    }

    public List<Name> getGlobalName() {
        return globalName;
    }

    public void BuildIR() {
        visit(program);
    }

    @Override
    public void visit(ProgNode node) {
        setIRScope(node.getScope().getIrScope());
        for(Name name : node.getScope().getScopeNodes().keySet()) {
            if(!node.getScope().getScopeNodes().get(name).isBuiltIn())
                globalName.add(name);
        }
        for(DeclNode item : node.getDeclares()) {
            visit(item);
        }
    }

    @Override
    public IRInstruction visit(DeclNode node) {
        if(node == null) return null;
        return node.accept(this);
    }

    @Override
    public IRInstruction visit(ClassDeclNode node) {
        curClass = node;
        Class classType = new Class(node.getName());
        if(node == null) return null;
        for(FuncDeclNode item : node.getMemberFunction())
            visit(item);
        for(VarDeclNode item : node.getMemberVarible()) {
//            visit(item);
            classType.addContain(convertType(item.getType()));
        }
        addType(classType);
        curClass = null;
        return null;
    }

    @Override
    public Function visit(FuncDeclNode node) {
        IRType type = visit(node.getReturnType());
        List<Parameter> params = new ArrayList<>();
        setIRScope(node.getInternalScope().getIrScope());
        curFunc = new FunctionScope(node.getName());
        curFunc.setFuncDeclNode(node);
        if(curClass != null)
            params.add(new Parameter(new Class(curClass.getName()), Name.getName("null") ,
                    new Address(Name.getName("null"), new BuiltIn()), true));
        for(FuncParamNode item : node.getParameter()) {
            Address address = new Address(item.getName(), convertType(item.getType()));
            curFunc.incSlotNum();
            params.add(new Parameter(convertType(item.getType()), item.getName(), address));
            curIRScope.addAddress(item.getName(), address);
        }
        Name name = FunctionRename(node.getName());
//        visitFormalParameter(node.getParameter());
        Function function = new Function(curLab, name, type, params, 0);
        addInst(function);
        visit(node.getBlock());
        exitIRScope();
        function.setUsedSlotNum(curFunc.getUsedSlotNum());
        return null;
    }

    @Override
    public Address visit(VarDeclNode node) {
        IRType irType = convertType(node.getType());
        Address address = new Address(node.getName(), irType);
        addInst(new Alloca(curLab, address,  irType));
        curFunc.incSlotNum();
        curIRScope.addAddress(node.getName(), address);
        if(node.getValue() != null) {
            if(node.getValue() instanceof ConditionExprNode ||
                    (node.getValue() instanceof UnaryExprNode &&
                    ((UnaryExprNode) node.getValue()).getOption() == UnaryOp.NOT)) {
                        this.addressForCond = address;
                        isVarForFond = true;
                        IntegerValue right = visit(node.getValue());
                        isVarForFond = false;
            }
            else {
                    IntegerValue right = visit(node.getValue());
                    addInst(new Store(curLab, address, right));
            }
        }
        return address;
    }

    @Override
    public IRType visit(FuncParamNode node) {
        return null;
    }

    @Override
    public IntegerValue visit(ExprNode node) {
        if(node == null) return null;
        return node.accept(this);
    }

    @Override
    public IntegerValue visit(AndExprNode node) {
        if(node == null) return null;
        Label true_label = new Label(null);
        Label false_label = nextLabel;
        IntegerValue lhs = visit(node.getLeft());

        Compare.Condition op = null;
        if(node.getLeft() instanceof BinaryExprNode)
            op = convertOp(((BinaryExprNode) node.getLeft()).getOp());
        addInst(new Branch(curLab, true_label, false_label, lhs, op));

        addInst(true_label);
        IntegerValue rhs = visit(node.getRight());
        return rhs;
    }

    @Override
    public IntegerValue visit(ArrayExprNode node) {
        //should return address
        IntegerValue index = visit(node.getIndex());
        Address array = (Address)visit(node.getArray());
        return new Address(array.getName(), array, index);
    }

    @Override
    public IntegerValue visit(AssignExprNode node) {
        IntegerValue left = visit(node.getLeft());
        IntegerValue right = visit(node.getRight());
        addInst(new Store(curLab, left, right));
        return null;
    }

    @Override
    public IntegerValue visit(BinaryExprNode node) {
        if(node.getL() instanceof StringConstNode)
            return dealStringOperation(node);
//        VirtualRegister dest = currentFunction.getRegister();
        IntegerValue left = visit(node.getL());
        IntegerValue right = visit(node.getR());
        VitualRegister dest = curFunc.getRegister();
        IRType irType = new BuiltIn();
        Address address = new Address(dest.getName(), irType);
        addInst(new Alloca(curLab, address, irType));
        curFunc.incSlotNum();
        curIRScope.addAddress(dest.getName(), address);

        addBinaryInst(address, left, right, node.getOp());
        return address;
    }

    @Override
    public IntegerValue visit(BoolConstNode node) {
        if(node.getBool())
            return new Immediate(1);
        else return new Immediate(0);
    }

    @Override
    public IntegerValue visit(CallExprNode node)
    {
        //should assign a register to the return value
//        Address address = new Address(currentFunction.getRegister().getName());
//        addInst(new Alloca(currentLabel, address, convertType(node.getFunction().getReturnType())));
        VitualRegister register = curFunc.getRegister();
        IRType irType = new BuiltIn();
        Address address = new Address(register.getName(), irType);
        addInst(new Alloca(curLab, address, irType));
        curFunc.incSlotNum();
        curIRScope.addAddress(register.getName(), address);

        List<IntegerValue> params = new ArrayList<>();
        for(ExprNode item : node.getParameter().getExpresses())
        {
            params.add(visit(item));
        }
        addInst(new Call(curLab, address, FunctionRename(node.getFuncName()), params));
        return address;
    }

    @Override
    public IntegerValue visit(ExprListNode node) {
        return null;
    }

    @Override
    public IntegerValue visit(IdentifierExprNode node) {
        if(node == null) return null;
        return curIRScope.findAddress(node.getName());
    }

    @Override
    public Immediate visit(IntConstNode node)
    {
        if(node == null) return null;
//        addInst(new Load(currentLabel, currentFunction.getRegister(), new Immediate(node.getValue())));
        return new Immediate(node.getInteger());
    }

    @Override
    public IntegerValue visit(MemberExprNode node)
    {
        //should return address
        IntegerValue ret;
        if(node == null) return null;
        Address base = (Address)visit(node.getExpress());
        if(node.isFunctionCall())
        {
            if(node.getExpress().getExprType() instanceof ClassType)
            {
                Name name = node.getExpress().getExprType().getTypeName();
                node.getFunctionCall().setFuncName(Name.getName("__" + name.toString() + "__" + node.getFunctionCall().getFuncName()));
            }
            ret = visit(node.getFunctionCall());
        }
        else
        {
            VarDeclNode var = (VarDeclNode)node.getExpress().getExprType().getClassNode().getInternalScope().findNode(node.getName());
            int offset = var.getMemberNum();
            ret = new Address(Name.getName("_" + node.getExpress().getExprType().getTypeName().toString() +
                    "_" + node.getName().toString()), base, new Immediate(offset));
        }

        return ret;
    }

    




    private void addBinaryInst(Address dest, IntegerValue left, IntegerValue right, BinaryOp op)
    {
        if(BinaryOp.isCompare(op))
        {
            Compare.Condition condition;
            switch(op)
            {
                case SLT: condition = Compare.Condition.SLT; break;
                case SGT: condition = Compare.Condition.SGT; break;
                case SEQ: condition = Compare.Condition.SEQ; break;
                case BEQ: condition = Compare.Condition.BEQ; break;
                case EQU: condition = Compare.Condition.EQU; break;
                case NEQ: condition = Compare.Condition.NEQ; break;
                default: condition = null; break;
            }
            addInst(new Compare(curLab, condition, dest, left, right));
        }
        else
        {
            wtlcompiler.IR.BinaryOp.BinOp binaryOp;
            switch (op)
            {
                case ADD: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.add; break;
                case MIN: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.sub; break;
                case MUL: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.imul; break;
                case DIV: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.idiv; break;
                case L_SHIFT: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.shl; break;
                case R_SHIFT: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.shr; break;
                case MOD: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.idiv; break;
                case BIT_AND: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.and; break;
                case BIT_OR: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.or; break;
                case BIT_XOR: binaryOp = wtlcompiler.IR.BinaryOp.BinOp.xor; break;
                default: binaryOp = null; break;
            }
            addInst(new wtlcompiler.IR.BinaryOp(curLab, binaryOp, dest, left, right));
        }
    }

    private IntegerValue dealStringOperation(BinaryExprNode node) {
        //TODO
        return null;
    }

    private Compare.Condition convertOp(BinaryOp op) {
        Compare.Condition condition;
        switch(op) {
            case SLT: condition = Compare.Condition.SLT; break;
            case SGT: condition = Compare.Condition.SGT; break;
            case SEQ: condition = Compare.Condition.SEQ; break;
            case BEQ: condition = Compare.Condition.BEQ; break;
            case EQU: condition = Compare.Condition.EQU; break;
            case NEQ: condition = Compare.Condition.NEQ; break;
            default: condition = null; break;
        }
        return condition;
    }

    private void addInst(IRInstruction instruction) {
        if(curInst == null) {
            curInst = instruction;
            entry = instruction;
            return;
        }
        instruction.setPrev(curInst);
        curInst.setNext(instruction);
        curInst = instruction;
        curLab.addInst(instruction);
    }

    private void exitIRScope() {
        this.curIRScope = curIRScope.getParent();
    }

    private Name FunctionRename(Name origin_name)
    {
        Name new_name = curClass == null? origin_name :
                Name.getName("__" + curClass.getName().toString() + "__" + origin_name.toString());
        return new_name;
    }

    private void addType(Class classType) {
        types.add(classType);
    }

    private IRType convertType(Type type) {
        //CAN OPTIMIZE
        //By building a map instead of new
        IRType irType;
        if(type instanceof BuiltInType)
            irType = new BuiltIn();
        else if(type instanceof ClassType)
            irType = new Class(type.getTypeName());
        else
            irType = new Array(convertType(((ArrayType)type).getBasicType()), null);
        return irType;
    }

    private void setIRScope(IRScope irScope) {
        this.curIRScope = irScope;
    }

    private void setNextLabel(Label label) {
        nextLabel = label;
        nextLabelStack.push(label);
    }
}
