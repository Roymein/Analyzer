package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Util;

/**
 * @see VT#CAST
 */
public class CastExpr extends Value.E1Expr {
    public String from;
    public String to;

    public CastExpr(Value value, String from, String to) {
        super(VT.CAST, value);
        this.from = from;
        this.to = to;
    }

    @Override
    protected void releaseMemory() {
        from = to = null;
        super.releaseMemory();
    }

    @Override
    public Value clone() {
        return new CastExpr(op.trim().clone(), from, to);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new CastExpr(op.clone(mapper), from, to);
    }

    @Override
    public String toString0() {
        return "((" + Util.toShortClassName(to) + ")" + op + ")";
    }
}
