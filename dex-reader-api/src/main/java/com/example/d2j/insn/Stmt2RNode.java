package com.example.d2j.insn;

import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class Stmt2RNode extends DexStmtNode {
    public final int a;
    public final int b;

    public Stmt2RNode(Op op, int a, int b) {
        super(op);
        this.a = a;
        this.b = b;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitStmt2R(op, a, b);
    }
}
