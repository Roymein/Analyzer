package com.example.d2j.insn;

import com.example.d2j.DexLabel;
import com.example.d2j.reader.Op;

public abstract class BaseSwitchStmtNode extends DexStmtNode {

    public final int a;
    public final DexLabel[] labels;

    protected BaseSwitchStmtNode(Op op, int a, DexLabel[] labels) {
        super(op);
        this.a = a;
        this.labels = labels;
    }
}
