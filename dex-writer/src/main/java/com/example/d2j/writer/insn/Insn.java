package com.example.d2j.writer.insn;

import java.nio.ByteBuffer;

public abstract class Insn {
    protected static final boolean DEBUG = false;
    /**
     * offset in codeUnit
     */
    public int offset;

    /**
     * size in codeUnit
     *
     * @return
     */
    abstract public int getCodeUnitSize();

    public void write(ByteBuffer out) {
        // TODO Auto-generated method stub

    }

    boolean isLabel() {
        return false;
    }
}
