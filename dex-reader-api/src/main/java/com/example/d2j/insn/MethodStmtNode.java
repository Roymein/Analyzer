package com.example.d2j.insn;

import com.example.d2j.Method;
import com.example.d2j.Proto;
import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class MethodStmtNode extends AbstractMethodStmtNode {
    public final Method method;

    public MethodStmtNode(Op op, int[] args, Method method) {
        super(op, args);
        this.method = method;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitMethodStmt(op, args, method);
    }

    @Override
    public Proto getProto() {
        return method.getProto();
    }
}
