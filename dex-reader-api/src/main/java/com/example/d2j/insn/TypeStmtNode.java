package com.example.d2j.insn;

import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class TypeStmtNode extends DexStmtNode {

    public final int a;
    public final int b;
    public final String type;

    public TypeStmtNode(Op op, int a, int b, String type) {
        super(op);
        this.a = a;
        this.b = b;
        this.type = type;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitTypeStmt(op, a, b, type);
    }
}
