package wtlcompiler.utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Name {
    private String name;
    private static Map<String, Name> nameList = new HashMap<>();

    public Name(String tmp) {
        name = tmp;
    }

    public static Name getName(String name) {
        Name s = nameList.get(name);
        if (s == null) {
            s = new Name(name);
            nameList.put(name, s);
        }
        return s;
    }

    @Override
    public String toString() {
        return name;
    }
}
