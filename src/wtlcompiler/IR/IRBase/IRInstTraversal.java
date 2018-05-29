package wtlcompiler.IR.IRBase;

import wtlcompiler.IR.*;

public interface IRInstTraversal {
    void visit(IRInstruction inst);
    void visit(Alloca inst);
    void visit(BinaryOp inst);
    void visit(Branch inst);
    void visit(Call inst);
    void visit(Compare inst);
    void visit(Function inst);
    void visit(Jump inst);
    void visit(Label inst);
//    void visit(Load inst);
    void visit(MemCopy inst);
    void visit(Move inst);
    void visit(Return inst);
    void visit(Store inst);
}
