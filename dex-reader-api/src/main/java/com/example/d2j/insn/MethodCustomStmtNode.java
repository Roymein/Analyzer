package com.example.d2j.insn;

import com.example.d2j.MethodHandle;
import com.example.d2j.Proto;
import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class MethodCustomStmtNode extends AbstractMethodStmtNode {
    public final String name;
    public final Proto proto;
    public final MethodHandle bsm;
    public final Object[] bsmArgs;

    public MethodCustomStmtNode(Op op, int[] args, String name, Proto proto, MethodHandle bsm, Object[] bsmArgs) {
        super(op, args);
        this.proto = proto;
        this.name = name;
        this.bsm = bsm;
        this.bsmArgs = bsmArgs;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitMethodStmt(op, args, name, proto, bsm, bsmArgs);
    }

    @Override
    public Proto getProto() {
        return proto;
    }
}
