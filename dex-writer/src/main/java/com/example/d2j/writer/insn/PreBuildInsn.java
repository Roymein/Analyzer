package com.example.d2j.writer.insn;

import java.nio.ByteBuffer;

public class PreBuildInsn extends Insn {

    final public byte[] data;

    public PreBuildInsn(byte[] data) {

        this.data = data;
    }


    @Override
    public int getCodeUnitSize() {
        return data.length / 2;
    }

    @Override
    public void write(ByteBuffer out) {
        out.put(data);
    }
}
