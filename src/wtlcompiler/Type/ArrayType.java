package wtlcompiler.Type;

public class ArrayType extends Type {
    private Type baseNodeType;
    private int dimension;

    public ArrayType(String name, int size, Type base) {
        super(name, 1);
//      baseType = base;
        if (size <= 1) {
            baseNodeType = base;
        }
        else {
            baseNodeType = new ArrayType(name, size  - 1, base);
        }
        dimension = size;
    }

    public ArrayType(Type base, int size) {
        super(base.getTypeName().toString(), 1);
        if (size <= 1) {
            baseNodeType = base;
        }
        else {
            baseNodeType = new ArrayType(base.getTypeName().toString(), size - 1, base);
        }
        dimension = size;
    }

    public int getDimension() {
        return dimension;
    }

    public final Type getBasicType() {
        return baseNodeType;
    }

    public final Type getBaseType() {
        Type base = getBasicType();
        while (base instanceof ArrayType) base = ((ArrayType)base).getBasicType();
        return base;
    }

}
