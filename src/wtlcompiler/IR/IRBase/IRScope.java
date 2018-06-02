package wtlcompiler.IR.IRBase;

import wtlcompiler.IR.Value.Address;
import wtlcompiler.IR.Value.VitualRegister;
import wtlcompiler.utility.Name;

import java.util.HashMap;
import java.util.Map;

public class IRScope {
    private Map<Name, VitualRegister> registerMap = new HashMap<>();
    private Map<Name, Address> addressMap = new HashMap<>();
    private boolean isTop;
    private IRScope parent;

    public IRScope(boolean isTop) {
        this.isTop = isTop;
    }

    public IRScope(IRScope parent) {
        this.isTop = false;
        this.parent = parent;
    }

    public void addRegister(Name name, VitualRegister vitualRegister) {
        //need to finish
        registerMap.put(name, vitualRegister);
    }

    public VitualRegister getRegister(Name name) {
        if (registerMap.containsKey(name))
            return registerMap.get(name);
        else return parent.getRegister(name);
    }



    public Address findAddress(Name name) {
        if (addressMap.containsKey(name))
            return addressMap.get(name);
        else
            return parent.findAddress(name);
    }

    public boolean containAddress(Name name) {
        if (addressMap.containsKey(name))
            return true;
        else if (this.parent == null)
            return false;
        else
            return this.parent.containAddress(name);
    }

    public void addAddress(Name name, Address address) {
        addressMap.put(name, address);
    }

    public IRScope getParent() {
        return parent;
    }
}
