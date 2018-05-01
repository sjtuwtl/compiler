package wtlcompiler.utility;

import org.antlr.v4.codegen.model.decl.Decl;
import wtlcompiler.AST.node.DeclNode.*;
import wtlcompiler.AST.object.*;
import wtlcompiler.utility.location;
import wtlcompiler.utility.Name;
import wtlcompiler.Type.*;

import java.util.*;

public class Scope {
    private Map<Name, DeclNode> scopeNodes = new HashMap<>();
    private Map<Name, Type> scopeTypes = new HashMap<>();
    private List<Scope> children = new ArrayList<>();
    private Scope parent;
    private boolean isTop;

    private boolean isFunction;
    private boolean isLoop;
    private boolean isClass;

    public Scope(boolean top) {
        isTop = top;
        isFunction = false;
        isLoop = false;
        parent = this;
        isClass = false;
    }

    public Scope(Scope s) {
        isTop = false;
        parent = s;
        isFunction = false;
        isLoop = false;
        isClass = false;
    }

    public void addNode(DeclNode node) throws RuntimeException {
        if (scopeNodes.containsKey(node.getName())) {
            throw new RuntimeException(node.getName().toString() + " have been declared at Line " + scopeNodes.get(node.getName()).getLocation().getLine());
        }
        if(!node.isFunction() && ((VarDeclNode)node).getType().getTypeName() == Name.getName("void"))
        {
            throw new RuntimeException("void can only be return type of a function");
        }
        scopeNodes.put(node.getName(), node);
    }

    public DeclNode findNode(Name name) throws RuntimeException {
        if(scopeNodes.containsKey(name))
            return scopeNodes.get(name);
        else if(this.isTop)
            throw new RuntimeException(name + " have not been declared");
        else return this.parent.findNode(name);
    }

    public boolean containsNode(Name name) {
        if(scopeNodes.containsKey(name))
            return true;
        else if(this.isTop)
            return false;
        else return this.parent.containsNode(name);
    }

    public void addType(ClassDeclNode node) throws RuntimeException {
        if(scopeTypes.containsKey(node.getName())) {
            throw new RuntimeException(node.getName() + " have been declared at "
                    + scopeTypes.get(node.getName()).getClassNode().getLocation());
        }
        scopeTypes.put(node.getName(), new ClassType(node));
    }

    public void addType(Type type) throws RuntimeException {
        if(scopeTypes.containsKey(type.getTypeName())) {
            throw new RuntimeException(type.getTypeName() + " have been declared at "
                    + scopeTypes.get(type.getTypeName()).getClassNode().getLocation());
        }
        scopeTypes.put(type.getTypeName(), type);
    }

    public Type findType(Name name) throws RuntimeException {
        if(scopeTypes.containsKey(name))
            return scopeTypes.get(name);
        else if(this.isTop)
            throw new RuntimeException(name + " have not been declared");
        else return this.parent.findType(name);
    }

    public boolean containsType(Name name) {
        if(scopeTypes.containsKey(name))
            return true;
        else if(this.isTop)
            return false;
        else return this.parent.containsType(name);
    }

    public Scope getParent() {
        if(this.isTop)
            return this;
        else return this.parent;
    }

    public void setFunction(boolean function) {
        isFunction = function;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public void setClass(boolean c) {
        isClass = c;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public boolean isTop() {
        return isTop;
    }

    public boolean isClass() {
        return isClass;
    }

    public void InitializeBuiltInType() {
        addType(new BuiltInType("int", 4));
        addType(new BuiltInType("bool", 1));
        addType(new BuiltInType("string", 1));
        addType(new BuiltInType("void", 0));
    }

    public void InitializeBuiltInFunction() {
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("getInt", new ArrayList<>(), new BuiltInType("int", 4)),
                null));
        List<FuncParamNode> paras = new ArrayList<>();
        paras.add(new FuncParamNode(new location(0,0), new ParameterObject("a", new Type("string", 0))));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("print", paras, new BuiltInType("void", 0)),
                null));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("println", paras, new BuiltInType("void", 0)),
                null));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("getString", new ArrayList<>(), new BuiltInType("string", 0)),
                null));
        paras.clear();
        paras.add(new FuncParamNode(new location(0, 0), new ParameterObject("a", new Type("int", 4))));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("toString", paras, new BuiltInType("string", 4)),
                null));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("size", new ArrayList<>(), new BuiltInType("int", 4)),
                null));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("length", new ArrayList<>(), new BuiltInType("int", 4)),
                null));
        paras.clear();
        paras.add(new FuncParamNode(new location(0, 0), new ParameterObject("left", new Type("int", 4))));
        paras.add(new FuncParamNode(new location(0, 0), new ParameterObject("right", new Type("int", 4))));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("substring", paras, new BuiltInType("string", 4)),
                null));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("parseInt", new ArrayList<>(), new BuiltInType("int", 4)),
                null));
        paras.clear();
        paras.add(new FuncParamNode(new location(0, 0), new ParameterObject("pos", new Type("int", 4))));
        addNode(new FuncDeclNode(new location(0, 0),
                new FuncDeclObject("ord", paras, new BuiltInType("int", 4)),
                null));
    }

    public void Initialize() {
        InitializeBuiltInType();
        InitializeBuiltInFunction();
    }
}
