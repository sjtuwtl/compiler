package wtlcompiler.Type;

import wtlcompiler.AST.node.DeclNode.ClassDeclNode;
import wtlcompiler.utility.Name;

public class Type {
    private Name typeName;
    private int typeSize;
    public Type(String name, int size){
        typeName = Name.getName(name);
        typeSize = size;
    }

    public Name getTypeName() {
        return typeName;
    }

    public int getTypeSize() {
        return typeSize;
    }

    public ClassDeclNode getClassNode() {
        return null;
    }

    public static boolean equal(Type a, Type b) {
        return (a.getTypeName() == b.getTypeName());
      //return (a.getTypeName() == b.getTypeName()) || (!(a instanceof BuiltInType) && b.getTypeName() == Name.getName("null"));
    }
}
