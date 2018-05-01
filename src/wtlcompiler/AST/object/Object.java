package wtlcompiler.AST.object;

import wtlcompiler.utility.Name;
import wtlcompiler.utility.location;

public class Object {
    protected Name name;

    public Object(String na) {
        name = Name.getName(na);
    }

    public Name getName() {
        return name;
    }
}
