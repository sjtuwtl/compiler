package wtlcompiler.Type;

import wtlcompiler.AST.node.DeclNode.ClassDeclNode;

public class ClassType extends Type{
    private ClassDeclNode classNode;
    public ClassType(String name, int size) {
        super(name, size);
    }

    public ClassType(ClassDeclNode node) {
        super(node.getName().toString(), 4);
        classNode = node;
    }

    @Override
    public  ClassDeclNode getClassNode() {
        return classNode;
    }
}
