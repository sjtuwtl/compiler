package wtlcompiler.IR.IRBase;

import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.node.ProgNode;
import wtlcompiler.AST.node.ExprNode.*;
import wtlcompiler.AST.node.StmtNode.*;
import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.Immediate;
import wtlcompiler.IR.Value.IntegerValue;
import wtlcompiler.IR.Value.VitualRegister;
import wtlcompiler.Translator.NasmInst;
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
    private IRInstruction initializeGlobalInst;
    private Label curLab;
    private IRScope curIRScope;
    private FunctionScope curFunc;
    private ClassDeclNode curClass;
    private boolean isHeapAllocate = false;
    private boolean isInitializeInst = false;

    private Label nextJumpLabel;
    private Label nextEndLabel;
    private boolean isVarForCond = false;
    private Stack<Label> jumpLabelStack = new Stack<>();
    private Stack<Label> endLabelStack = new Stack<>();

    private IRInstruction entry;
    private IRInstruction initializeEntry;
    private ProgNode program;
    private List<Class> types = new ArrayList<>();
    private DataSection dataSection = new DataSection();
    private DataSection dataZone = new DataSection();
    private DataSection bssZone = new DataSection();
    private List<Name> globalName = new ArrayList<>();

    public IRConstructor(ProgNode progNode) {
        program = progNode;
        Label globalLabel = new Label("GLOBAL");
        curFunc = new FunctionScope(Name.getName("GLOBAL"));
        curLab = globalLabel;
        setJumpLabel(globalLabel);
        setEndLabel(globalLabel);
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

    public IRInstruction getInitializeEntry() {
        return initializeEntry;
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
            if (item instanceof VarDeclNode) {
                addGlobalVar((VarDeclNode) item);
                continue;
            }
            visit(item);
            if(item instanceof FuncDeclNode)
                if(((FuncDeclNode) item).getReturnType().getTypeName() == Name.getName("void"))
                    addInst(new Return(curLab, null));

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
        addInst(new Return(curLab, null));
        return null;
    }

    @Override
    public Address visit(VarDeclNode node) {
        IRType irType = convertType(node.getVar().getType());
        String name = node.getName().toString();
        if (node.getName() == Name.getName("ch") || node.getName() == Name.getName("dx"))
            name += "_ch";
        Address address = new Address(Name.getName(name), irType);
        addInst(new Alloca(curLab, address,  irType));
        curFunc.incSlotNum();
        curIRScope.addAddress(node.getName(), address);
        if(node.getValue() != null) {
/*            if(isCondition(node.getValue())) {
                isVarForCond = true;
                Label trueLabel = new Label(null);
                Label falseLabel = new Label(null);
                Label endLabel = new Label(null);
                setTrueLabel(trueLabel);
                setFalseLabel(falseLabel);
                addInst(trueLabel);
                curLab = trueLabel;
                addInst(new Store(curLab, address, new Immediate(1)));
                addInst(new Jump(curLab, endLabel));
                addInst(falseLabel);
                addInst(new Store(curLab, address, new Immediate(0)));
//                addInst(new Jump(currentLabel, endLabel));
                addInst(endLabel);
                isVarForCond = false;
                exitTrueLabel();
                exitFalseLabel();
//                addInst(new Compare(currentLabel, condition, dest, dest, new Immediate(0)));
            }
            else {
                IntegerValue right = visit(node.getValue());
                addInst(new Store(curLab, address, right));
            }*/
            IntegerValue right = visit(node.getValue());
            addInst(new Store(curLab, address, right));
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
        Label midLabel = new Label(null);
        Label trueLabel = new Label(null);
        Label falseLabel = new Label(null);
        Label endLabel = new Label(null);

        IntegerValue lhs = visit(node.getLeft());
        /*Compare.Condition op = null;
        if(node.getLeft() instanceof BinaryExprNode)
            op = convertOp(((BinaryExprNode) node.getLeft()).getOp());
        addInst(new Branch(curLab, midLabel, falseLabel, lhs, op));*/
        Address address = new Address(curFunc.getRegister().getName(), new BuiltIn());
        addInst(new Compare(curLab, Compare.Condition.NEQ,
                address,
                lhs, new Immediate(0)));
        addInst(new Branch(curLab, midLabel, falseLabel, address));
        addInst(midLabel);
        curLab = midLabel;
        IntegerValue rhs = visit(node.getRight());
        /*Compare.Condition op = null;
        if(node.getRight() instanceof BinaryExprNode)
            op = convertOp(((BinaryExprNode) node.getRight()).getOp());*/
        Address returnAddress = new Address(curFunc.getRegister().getName(), new BuiltIn());
        addInst(new Alloca(curLab, returnAddress, new BuiltIn()));
        curFunc.incSlotNum();
        //addInst(new Branch(curLab, trueLabel, falseLabel, rhs, op));
        addInst(new Compare(curLab, Compare.Condition.NEQ,
                returnAddress,
                rhs, new Immediate(0)));
        addInst(new Branch(curLab, trueLabel, falseLabel, returnAddress));
        storeCompareResult(returnAddress, trueLabel, falseLabel, endLabel);
        return returnAddress;
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
        IntegerValue right;
        /*if(isCondition(node.getRight())) {
            isVarForCond = true;
            Label trueLabel = new Label(null);
            Label falseLabel = new Label(null);
            Label endLabel = new Label(null);
            setFalseLabel(falseLabel);
            setTrueLabel(trueLabel);
            right = visit(node.getRight());
            addInst(new Branch(curLab, nextTrueLabel, nextFalseLabel, right));
            addInst(trueLabel);
            curLab = trueLabel;
            addInst(new Store(curLab, left, new Immediate(1)));
            addInst(new Jump(curLab, endLabel));
            addInst(falseLabel);
            addInst(new Store(curLab, left, new Immediate(0)));
            addInst(endLabel);
            isVarForCond = false;
            exitTrueLabel();
            exitFalseLabel();
        }
        else
        {
            right = visit(node.getRight());
            addInst(new Store(curLab, left, right));
        }*/
        right = visit(node.getRight());
        addInst(new Store(curLab, left, right));
        return null;
    }

    @Override
    public IntegerValue visit(BinaryExprNode node) {
        if(node.getL().getExprType().getTypeName() == Name.getName("string"))
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
        Address address = null;
        if (node.getFunction().getReturnType() != null && node.getFunction().getReturnType().getTypeName() != Name.getName("void")) {
            address = new Address(register.getName(), irType);
            addInst(new Alloca(curLab, address, irType));
            curFunc.incSlotNum();
            curIRScope.addAddress(register.getName(), address);
        }

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
    public Immediate visit(IntConstNode node) {
        if(node == null) return null;
//        addInst(new Load(currentLabel, currentFunction.getRegister(), new Immediate(node.getValue())));
        return new Immediate(node.getInteger());
    }

    @Override
    public IntegerValue visit(MemberExprNode node) {
        //should return address
        IntegerValue ret;
        if(node == null) return null;
        Address base = (Address)visit(node.getExpress());
        if(node.isFunctionCall()) {
            if(node.getExpress().getExprType() instanceof ClassType) {
                Name name = node.getExpress().getExprType().getTypeName();
                node.getFunctionCall().setFuncName(Name.getName("__" + name.toString() + "__" + node.getFunctionCall().getFuncName()));
            }
            node.getFunctionCall().addParam(node.getExpress(), 0);
            ret = visit(node.getFunctionCall());
        }
        else {
            //TO DO
            VarDeclNode var = (VarDeclNode)node.getExpress().getExprType().getClassNode().getInternalScope().findNode(node.getName());
            int offset = var.getMemberNum();
            ret = new Address(node.getName(), base, new Immediate(offset));
        }

        return ret;
    }

    @Override
    public IntegerValue visit(NewExprNode node) {
        if(node == null) return null;
        return visit(node.getCreatorNode());
//        return null;
    }

    @Override
    public IntegerValue visit(CreatorExprNode node) {
        if(node == null)
            return null;
        if (node.getSize() == 0) {
            IRType irType = convertType(node.getType());
            Address address = new Address(curFunc.getRegister().getName(), irType);
            addInst(new Alloca(curLab, address, new Immediate(8)));
            curFunc.incSlotNum();
            addInst(new Malloc(curLab, new Immediate(node.getExprType().getTypeSize()), address));
            return address;
        }
        else if (node.getSize() == 1) {
            IRType irType = convertType(node.getType());
            Address address = new Address(curFunc.getRegister().getName(), irType);
            addInst(new Alloca(curLab, address, new BuiltIn()));
            curFunc.incSlotNum();
            IntegerValue value = visit(node.getExpresses().get(0));
            addInst(new Malloc(curLab,value,address));
            addInst(new Store(curLab, new Address(Name.getName(address.getName().toString() + "_size"),
                    address, new Immediate(-1)), value));
            return address;
        }
        else {
            Address address = new Address(curFunc.getRegister().getName(), convertType(node.getType()));
            memoryAllocate(node.getExpresses(), node.getType(), true, address);
            return address;
        }
    }

    @Override
    public IntegerValue visit(NullConstNode node) {
        return null;
    }

    @Override
    public IntegerValue visit(OrExprNode node) {
        if(node == null) return null;
        Label midLabel = new Label(null);
        Label trueLabel = new Label(null);
        Label falseLabel = new Label(null);
        Label endLabel = new Label(null);
        IntegerValue lhs = visit(node.getLeft());
        /*Compare.Condition op = null;
        if(node.getLeft() instanceof BinaryExprNode)
            op = convertOp(((BinaryExprNode) node.getLeft()).getOp());
        addInst(new Branch(curLab, trueLabel, midLabel, lhs, op));*/
        Address address = new Address(curFunc.getRegister().getName(), new BuiltIn());
        addInst(new Compare(curLab, Compare.Condition.NEQ,
                address,
                lhs, new Immediate(0)));
        addInst(new Branch(curLab, trueLabel, midLabel, address));
        addInst(midLabel);
        curLab = midLabel;
        IntegerValue rhs = visit(node.getRight());
        /*Compare.Condition op = null;
        if(node.getRight() instanceof BinaryExprNode)
            op = convertOp(((BinaryExprNode) node.getRight()).getOp());*/
        Address returnAddress = new Address(curFunc.getRegister().getName(), new BuiltIn());
        addInst(new Alloca(curLab, returnAddress, new BuiltIn()));
        curFunc.incSlotNum();
        //addInst(new Branch(curLab, trueLabel, falseLabel, rhs, op));
        addInst(new Compare(curLab, Compare.Condition.NEQ,
                returnAddress,
                rhs, new Immediate(0)));
        addInst(new Branch(curLab, trueLabel, falseLabel, returnAddress));
        storeCompareResult(returnAddress, trueLabel, falseLabel, endLabel);
        return returnAddress;
    }

    @Override
    public IntegerValue visit(PrefixExprNode node) {
        if ((node.getExpr() instanceof ConditionExprNode)
                || (node.getExpr() instanceof BinaryExprNode &&
                BinaryOp.isCompare(((BinaryExprNode) node.getExpr()).getOp())))
            return visitNotCondition(node);
        IntegerValue value = visit(node.getExpr());
        VitualRegister register = curFunc.getRegister();
        Address address = new Address(register.getName(), new BuiltIn());
        switch (node.getOption()) {
            case NEG:
                addInst(new Alloca(curLab, address, new BuiltIn()));
                curFunc.incSlotNum();
                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.neg, address,
                        value,null));
                return address;
            case NOT:
                addInst(new Alloca(curLab, address, new BuiltIn()));
                curFunc.incSlotNum();
                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.xor, address,
                        value, new Immediate(1)));
                return address;
            case BIT_NOT:
                addInst(new Alloca(curLab, address, new BuiltIn()));
                curFunc.incSlotNum();
                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.not, address,
                        value, new Immediate(-1)));
                return address;
            case POS:
                return value;
            case DEC:
                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.sub, (Address) value,
                        value, new Immediate(1)));
                addInst(new Store(curLab, value, register));
                return value;
            case INC:
                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.add, (Address) value,
                        value, new Immediate(1)));
                addInst(new Store(curLab, value, register));
                return value;
        }
//        return register;
        throw new RuntimeException("you should not be here");
    }

    @Override
    public IntegerValue visit(StringConstNode node) {
        //TODO
        //Address type
        StringBuilder builder = new StringBuilder();
        char[] chars = node.getStr().toCharArray();
        int length = node.getStr().length();
        boolean jump = false;
        for (int i = 0; i < length; ++i) {
            if (jump) {
                jump = false;
                continue;
            }
            if (chars[i] == '\\') {
                jump = true;
                if (chars[i + 1] == 'n')
                    builder.append('\n');
                else if (chars[i + 1] == '"')
                    builder.append('\"');
                else if (chars[i + 1] == '\\')
                    builder.append('\\');
            }
            else builder.append(chars[i]);
        }
        node.setStr(builder.toString());
        String name = dataSection.addData(node.getStr());
        return new Address(Name.getName(name), new BuiltIn());
    }

    @Override
    public IntegerValue visit(SuffixExprNode node) {
        IntegerValue value = visit(node.getExpr());
        VitualRegister new_reg = curFunc.getRegister();
        VitualRegister origin_reg = curFunc.getRegister();
        IRType irType;
        irType = new BuiltIn();

        Address origin_address = new Address(origin_reg.getName(), irType);

        addInst(new Alloca(curLab, origin_address, irType));
        curFunc.incSlotNum();
        curIRScope.addAddress(origin_reg.getName(), origin_address);
        addInst(new Store(curLab, origin_address, value));

        switch(node.getOption()) {
            case SUF_DEC:

                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.sub,
                        (Address) value, value, new Immediate(1)));
                return origin_address;
            case SUF_INC:
                addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.add,
                        (Address) value, value, new Immediate(1)));
                return origin_address;
        }
        throw new RuntimeException("you should not be here");
    }

    @Override
    public IntegerValue visit(ThisExprNode node) {
        //TODO
        return null;
    }

    @Override
    public IRInstruction visit(StmtNode node) {
        if(node == null) return null;
        return node.accept(this);
    }

    @Override
    public IRInstruction visit(BlockNode node) {
//        List<IRInstruction> block = new ArrayList<>();
        setIRScope(node.getScope().getIrScope());
        for(StmtNode item : node.getStatements()) {
//            block.add(visit(item));
            visit(item);
        }
        exitIRScope();
//        return new BasicBlock(block);
        return null;
    }

    @Override
    public IRInstruction visit(BreakNode node) {
        IRInstruction inst = new Jump(curLab, nextEndLabel);
        addInst(inst);
        return inst;
    }

    @Override
    public IRInstruction visit(ContinueNode node) {
        IRInstruction inst = new Jump(curLab, nextJumpLabel);
        addInst(inst);
        return inst;
    }

    @Override
    public IRInstruction visit(ForNode node) {
        setIRScope(node.getInternalScope().getIrScope());

        Label trueLabel = new Label(null);
        Label falseLabel = new Label(null);
        Label conditionLabel = new Label(null);
        Label jumpLabel = new Label(null);
        setJumpLabel(jumpLabel);
        setEndLabel(falseLabel);
        IntegerValue endCondition = null;
        if(node.getBeginCondition() != null)
            visit(node.getBeginCondition());
        addInst(conditionLabel);
        if(node.getEndCondition() != null)
            endCondition = visit(node.getEndCondition());
        if (endCondition != null) {
            addInst(new Compare(curLab, Compare.Condition.EQU, new Address(curFunc.getRegister().getName(), new BuiltIn()), endCondition, new Immediate(1)));
            addInst(new Branch(curLab, trueLabel, falseLabel, endCondition));
        }
        addInst(trueLabel);
        visit(node.getBlock());
        addInst(jumpLabel);
        visit(node.getUpdate());
        addInst(new Jump(curLab, conditionLabel));
        addInst(falseLabel);

        exitJumpLabel();
        exitIRScope();

        return null;
    }

    @Override
    public IRInstruction visit(IfNode node) {
        setIRScope(node.getInternalScope().getIrScope());

        Label trueLabel = new Label(null);
        Label falseLabel = new Label(null);
        Label endLabel = new Label(null);

        IntegerValue condition = visit(node.getCondition());
        addInst(new Compare(curLab, Compare.Condition.EQU, new Address(curFunc.getRegister().getName(), new BuiltIn()), condition, new Immediate(1)));
        addInst(new Branch(curLab, trueLabel, falseLabel, condition));
        addInst(trueLabel);
        visit(node.getState());
        addInst(new Jump(curLab, endLabel));
        addInst(falseLabel);
        if(node.getElsestate() != null)
            visit(node.getElsestate());
        addInst(new Jump(curLab, endLabel));
        addInst(endLabel);

        exitIRScope();
        return null;
    }

    @Override
    public IRInstruction visit(ReturnNode node) {
        IntegerValue integerValue = visit(node.getExpress());
        addInst(new Return(curLab, integerValue));
        return null;
    }

    @Override
    public IRInstruction visit(WhileNode node)
    {
        setIRScope(node.getInternalScope().getIrScope());

        Label trueLabel = new Label(null);
        Label falseLabel = new Label(null);
        Label conditionLabel = new Label(null);
        setJumpLabel(conditionLabel);
        setEndLabel(falseLabel);

        addInst(conditionLabel);
        IntegerValue endCondition = null;
        if(node.getCondition() != null)
            endCondition = visit(node.getCondition());
        if (endCondition != null) {
            addInst(new Compare(curLab, Compare.Condition.EQU, new Address(curFunc.getRegister().getName(), new BuiltIn()), endCondition, new Immediate(1)));
            addInst(new Branch(curLab, trueLabel, falseLabel, endCondition));
        }
        addInst(trueLabel);
        visit(node.getBlock());
        addInst(new Jump(conditionLabel, conditionLabel));
        addInst(falseLabel);

        exitIRScope();
        exitJumpLabel();
        exitEndLabel();
        return null;
    }

    @Override
    public IRInstruction visit(ExprStatNode node) {
        if(node == null) return null;
        visit(node.getExpress());
        return null;
    }

    @Override
    public IRType visit(Type type) {
        return convertType(type);
    }



    private void addBinaryInst(Address dest, IntegerValue left, IntegerValue right, BinaryOp op) {
        if(BinaryOp.isCompare(op)) {
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
            addInst(new Compare(curLab, condition, dest, left, right));
            Label trueLabel = new Label(null);
            Label falseLabel = new Label(null);
            Label endLabel = new Label(null);
            addInst(new Branch(curLab, trueLabel, falseLabel, dest, condition));
            storeCompareResult(dest, trueLabel, falseLabel, endLabel);
        }
        else {
            wtlcompiler.IR.BinaryOp.BinOp binaryOp;
            switch (op) {
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
        Address address = new Address(curFunc.getRegister().getName(), new BuiltIn());
        addInst((new Alloca(curLab, address, new BuiltIn())));
        curFunc.incSlotNum();
        List<IntegerValue> parameter = new ArrayList<>();
        parameter.add(visit(node.getL()));
        parameter.add(visit(node.getR()));
        switch (node.getOp()) {
            case ADD:
                addInst(new Call(curLab, address, Name.getName("StrADD"), parameter));
                break;
            case EQU:
                addInst(new Call(curLab, address, Name.getName("StrEQ"), parameter));
                break;
            case SLT:
                addInst(new Call(curLab, address, Name.getName("StrLT"), parameter));
                break;
            case SGT:
                addInst(new Call(curLab, address, Name.getName("StrGT"), parameter));
                break;
            case SEQ:
                addInst(new Call(curLab, address, Name.getName("StrLE"), parameter));
                break;
            case BEQ:
                addInst(new Call(curLab, address, Name.getName("StrGE"), parameter));
                break;
        }
        return address;
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
        if (isInitializeInst) {
            addInitializeInst(instruction);
            return;
        }
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
        if (type instanceof BuiltInType)
            irType = new BuiltIn();
        else if (type instanceof ClassType)
            irType = new Class(type.getTypeName());
        else if (type instanceof  ArrayType)
            irType = new Array(convertType(((ArrayType)type).getBasicType()), null);
        else irType = new IRType();
        return irType;
    }

    private void setIRScope(IRScope irScope) {
        this.curIRScope = irScope;
    }

    private void exitIRScope() {
        this.curIRScope = curIRScope.getParent();
    }

    private void setJumpLabel(Label label)
    {
        nextJumpLabel = label;
        jumpLabelStack.push(label);
    }

    private void exitJumpLabel()
    {
        jumpLabelStack.pop();
        nextJumpLabel = jumpLabelStack.peek();
    }

    private void setEndLabel(Label label) {
        nextEndLabel = label;
        endLabelStack.push(label);
    }

    private void exitEndLabel() {
        endLabelStack.pop();
        nextEndLabel = endLabelStack.peek();
    }

    public DataSection getBssZone() {
        return bssZone;
    }

    public DataSection getDataZone() {
        return dataZone;
    }

    private void addGlobalVar(VarDeclNode node) {
        String name = node.getName().toString();
        if (node.getName() == Name.getName("ch") || node.getName() == Name.getName("dx"))
            name += "_mine";
        Address address = new Address(Name.getName(name), true);
        if (node.getVar() == null)
            bssZone.addData(name, null);
        else {
            if (node.getType() instanceof BuiltInType) {
                if (node.getValue() instanceof IntConstNode) {
                    Immediate value = (Immediate) visit(node.getValue());
                    dataZone.addData(name, String.valueOf(value.getValue()), "dq");
                }
                else {
                    isInitializeInst = true;
                    IntegerValue value = visit(node.getValue());
                    addInst(new Store(curLab, address, value));
                    bssZone.addData(name, null);
                    isInitializeInst = false;
                }
            }
            else {
                isHeapAllocate = true;
                isInitializeInst = true;
                IntegerValue value = visit(node.getValue());
                addInst(new MemCopy(curLab, (Address) value, address));
                bssZone.addData(name, null);
                isInitializeInst = false;
                isHeapAllocate = false;
                address.setPointer(true);
            }
        }
        curIRScope.addAddress(node.getName(),address);
    }

    private Address memoryAllocate(List<ExprNode> node, Type type, boolean isTop, Address address) {
        IRType irType = node.size() == 0 ? new BuiltIn() : convertType(type);
        IntegerValue size = visit(node.get(0));
        node.remove(0);
        if (isTop) {
            curFunc.incSlotNum();
            addInst(new Alloca(curLab, address, new BuiltIn()));
        }
        addInst(new Malloc(curLab, size, address));
        addInst(new Store(curLab, new Address(Name.getName(address.getName().toString() + "_size"), address, new Immediate(-1)), size));
        Label trueLabel = new Label(null);
        Label falseLabel = new Label(null);
        Address offset = new Address(curFunc.getRegister().getName(), new BuiltIn());
        addInst(new Alloca(curLab, offset, new BuiltIn()));
        curFunc.incSlotNum();
        addInst(new Store(curLab, offset, new Immediate(0)));
        if (node.size() != 0) {
            addInst(trueLabel);
            Address address1 = new Address(curFunc.getRegister().getName(), address, offset);
            memoryAllocate(node, type, false, address1);
            Address compare = new Address(curFunc.getRegister().getName(), new BuiltIn());
            addInst(new Alloca(curLab, compare,new BuiltIn()));
            curFunc.incSlotNum();
            addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.add, offset, offset, new Immediate(1)));
            addInst(new Compare(curLab, Compare.Condition.SEQ, compare, offset, size));
            addInst(new Branch(curLab, trueLabel, falseLabel, compare, Compare.Condition.SEQ));
            addInst(falseLabel);
        }
        return address;
    }

    private void addInitializeInst(IRInstruction instruction) {
        if (initializeGlobalInst == null) {
            initializeGlobalInst = instruction;
            initializeEntry = instruction;
            return;
        }
        instruction.setPrev(initializeGlobalInst);
        initializeGlobalInst.setNext(instruction);
        initializeGlobalInst = instruction;
    }

    private boolean isCondition(ExprNode node) {
        return ((node instanceof ConditionExprNode)
                || (node instanceof BinaryExprNode &&
                BinaryOp.isCompare(((BinaryExprNode) node).getOp()))
                || (node instanceof UnaryExprNode &&
                ((UnaryExprNode) node).getOption() == UnaryOp.NOT));

    }

    private IntegerValue visitNotCondition(PrefixExprNode node) {
        IntegerValue value = visit(node.getExpr());
        VitualRegister register = curFunc.getRegister();

        Address address = new Address(register.getName(), new BuiltIn());

        if (node.getOption() != UnaryOp.NOT)
            throw new RuntimeException("should not be here");
        addInst(new Alloca(curLab, address, new BuiltIn()));
        addInst(new wtlcompiler.IR.BinaryOp(curLab, wtlcompiler.IR.BinaryOp.BinOp.xor, address, address, new Immediate(1)));
        return address;
    }

    private void storeCompareResult(Address address, Label trueLabel, Label falseLabel, Label endLabel) {
        addInst(trueLabel);
        addInst(new Store(curLab, address, new Immediate(1)));
        addInst(new Jump(curLab, endLabel));
        addInst(falseLabel);
        addInst(new Store(curLab, address, new Immediate(0)));
        addInst(endLabel);
    }
}
