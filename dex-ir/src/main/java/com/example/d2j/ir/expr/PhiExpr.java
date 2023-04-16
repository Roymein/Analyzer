package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;

public class PhiExpr extends Value.EnExpr {

    public PhiExpr(Value[] ops) {
        super(VT.PHI, ops);
    }

    @Override
    public Value clone() {
        return new PhiExpr(cloneOps());
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new PhiExpr(cloneOps(mapper));
    }

    @Override
    public String toString0() {
        StringBuilder sb = new StringBuilder("φ(");
        boolean first = true;
        for (Value vb : ops) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(vb);
        }
        sb.append(")");
        return sb.toString();
    }

}
