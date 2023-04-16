package com.example.d2j.insn;

import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class FillArrayDataStmtNode extends DexStmtNode {

    public final int ra;
    public final Object array;

    public FillArrayDataStmtNode(Op op, int ra, Object array) {
        super(op);
        this.ra = ra;
        this.array = array;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitFillArrayDataStmt(op, ra, array);
    }
}
