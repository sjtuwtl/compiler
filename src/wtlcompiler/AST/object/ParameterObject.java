package wtlcompiler.AST.object;

import wtlcompiler.Type.Type;

public class ParameterObject extends VarObject {
    public ParameterObject(String name, Type type) {
        super(name, false, type);
    }
}
