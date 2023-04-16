package com.example.d2j.writer.insn;

import com.example.d2j.reader.Op;

public abstract class OpInsn extends Insn {
    public Op op;


    public OpInsn(Op op) {
        this.op = op;
    }

    final public boolean isLabel() {
        return true;
    }

    @Override
    public int getCodeUnitSize() {
        return op.format.size;
    }
}