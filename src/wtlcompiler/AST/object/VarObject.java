package wtlcompiler.AST.object;

import wtlcompiler.Type.Type;
import wtlcompiler.utility.Scope;

public class VarObject extends Object{
    private Type type;
    private boolean isGlobal;
    private Scope scope;

    public VarObject(String name, boolean global, Type ty) {
        super(name);
        isGlobal = global;
        type = ty;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type ty) {
        type = ty;
    }
}
