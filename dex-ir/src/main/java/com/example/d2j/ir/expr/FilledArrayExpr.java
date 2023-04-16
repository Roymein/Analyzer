package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Util;

/**
 * Represent a FILLED_ARRAY expression.
 *
 * @see VT#FILLED_ARRAY
 */
public class FilledArrayExpr extends Value.EnExpr {

    public String type;

    @Override
    protected void releaseMemory() {
        type = null;
        super.releaseMemory();
    }

    public FilledArrayExpr(Value[] datas, String type) {
        super(VT.FILLED_ARRAY, datas);
        this.type = type;
    }

    @Override
    public Value clone() {
        return new FilledArrayExpr(cloneOps(), type);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new FilledArrayExpr(cloneOps(mapper), type);
    }

    @Override
    public String toString0() {
        StringBuilder sb = new StringBuilder().append("new ").append(Util.toShortClassName(type)).append("[]{");
        for (int i = 0; i < ops.length; i++) {
            sb.append(ops[i]).append(", ");
        }
        if (ops.length > 0) {
            sb.setLength(sb.length() - 2); // remove tail ", "
        }
        sb.append('}');
        return sb.toString();
    }
}
