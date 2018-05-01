package wtlcompiler.utility;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.TerminalNode;

public class location {
    private final int line;
    private final int column;
    private final Interval interval;

    public location(int line, int column) {
        this.line = line;
        this.column = column;
        interval = null;
    }

    public location(int line, int column, Interval interval) {
        this.line = line;
        this.column = column;
        this.interval = interval;
    }

    public location(Token token) {
        this.line = token.getLine();
        this.column = token.getCharPositionInLine();
        this.interval = new Interval(token.getTokenIndex(), token.getTokenIndex());
    }

    public location(ParserRuleContext ctx) {
        this.line = ctx.start.getLine();
        this.column = ctx.start.getCharPositionInLine();
        this.interval = ctx.getSourceInterval();
    }

    public location(TerminalNode terminalNode) {
        this(terminalNode.getSymbol());
    }

    public int getLine() {
        return line;
    }

    public final String getInfor() {
        String infor = "line " + line;
        return infor;
    }

    @Override
    public String toString() {
        return "line:" + String.valueOf(line) + "column:" + String.valueOf(column);
    }

    public Interval getInterval() {
        return interval;
    }
}
