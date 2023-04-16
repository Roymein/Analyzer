package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.ir.Util;

public class NewExpr extends Value.E0Expr {

    public String type;

    public NewExpr(String type) {
        super(VT.NEW);
        this.type = type;
    }

    @Override
    public Value clone() {
        return new NewExpr(type);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new NewExpr(type);
    }

    @Override
    protected void releaseMemory() {
        type = null;
        super.releaseMemory();
    }

    @Override
    public String toString0() {
        return "NEW " + Util.toShortClassName(type);
    }
}
