package com.example.d2j.insn;

import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;


public abstract class DexStmtNode {
    public final Op op;

    public int __index;

    protected DexStmtNode(Op op) {
        this.op = op;
    }

    public abstract void accept(DexCodeVisitor cv);
}
