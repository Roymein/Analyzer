package com.example.d2j.ir.expr;

import com.example.d2j.ir.LabelAndLocalMapper;
import com.example.d2j.MethodHandle;
import com.example.d2j.Proto;

public class InvokeCustomExpr extends AbstractInvokeExpr {
    public String name;
    public Proto proto;
    public MethodHandle handle;
    public Object[] bsmArgs;

    @Override
    protected void releaseMemory() {
        name = null;
        proto = null;
        handle = null;
        bsmArgs = null;
        super.releaseMemory();
    }

    @Override
    public Proto getProto() {
        return proto;
    }

    public InvokeCustomExpr(VT type, Value[] args, String methodName, Proto proto, MethodHandle handle, Object[] bsmArgs) {
        super(type, args);
        this.proto = proto;
        this.name = methodName;
        this.handle = handle;
        this.bsmArgs = bsmArgs;
    }

    @Override
    public Value clone() {
        return new InvokeCustomExpr(vt, cloneOps(), name, proto, handle, bsmArgs);
    }

    @Override
    public Value clone(LabelAndLocalMapper mapper) {
        return new InvokeCustomExpr(vt, cloneOps(mapper), name, proto, handle, bsmArgs);
    }

    @Override
    public String toString0() {
        StringBuilder sb = new StringBuilder();

        sb.append("InvokeCustomExpr(....)");
        return sb.toString();
    }
}
