package com.example.d2j.ir.expr;


import com.example.d2j.Proto;

public abstract class AbstractInvokeExpr extends Value.EnExpr {
    @Override
    protected void releaseMemory() {
        super.releaseMemory();
    }

    public abstract Proto getProto();

    public AbstractInvokeExpr(VT type, Value[] args) {
        super(type, args);
    }

}
