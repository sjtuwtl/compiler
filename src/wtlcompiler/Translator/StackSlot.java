package wtlcompiler.Translator;

public class StackSlot {
    public enum SlotType{
        dword, qword
    }

    private int beginPos;
    private int endPos;
    private SlotType type;
    private int paramPos;

    public StackSlot(int beingPos, int endPos, SlotType type) {
        this.beginPos = beingPos;
        this.endPos = endPos;
        this.type = type;
        paramPos = -1;
    }

    public StackSlot(int paramPos, SlotType type) {
        this.paramPos = paramPos;
        this.type = type;
    }

    public int getBeginPos() {
        return beginPos;
    }

    public int getEndPos() {
        return endPos;
    }

    @Override
    public String toString() {
        if (this.paramPos == -1)
            return type.toString() + " [rbp-" + String.valueOf(beginPos) + "]";
        else
            return type.toString() + " [rbp+" + String.valueOf(paramPos) + "]";
    }
}
