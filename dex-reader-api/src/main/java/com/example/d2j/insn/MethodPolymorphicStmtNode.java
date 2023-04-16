package com.example.d2j.insn;

import com.example.d2j.Method;
import com.example.d2j.Proto;
import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class MethodPolymorphicStmtNode extends AbstractMethodStmtNode {
    public final Method method;
    public final Proto proto;

    public MethodPolymorphicStmtNode(Op op, int[] args, Method method, Proto proto) {
        super(op, args);
        this.method = method;
        this.proto = proto;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitMethodStmt(op, args, method, proto);
    }

    @Override
    public Proto getProto() {
        return proto;
    }
}
