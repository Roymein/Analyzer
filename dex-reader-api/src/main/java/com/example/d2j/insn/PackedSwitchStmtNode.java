package com.example.d2j.insn;

import com.example.d2j.DexLabel;
import com.example.d2j.reader.Op;
import com.example.d2j.visitors.DexCodeVisitor;

public class PackedSwitchStmtNode extends BaseSwitchStmtNode {

    public final int first_case;

    public PackedSwitchStmtNode(Op op, int a, int first_case, DexLabel[] labels) {
        super(op, a, labels);
        this.first_case = first_case;
    }

    @Override
    public void accept(DexCodeVisitor cv) {
        cv.visitPackedSwitchStmt(op, a, first_case, labels);
    }
}
