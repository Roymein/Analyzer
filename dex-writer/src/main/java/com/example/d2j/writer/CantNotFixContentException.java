package com.example.d2j.writer;

import com.example.d2j.reader.Op;

public class CantNotFixContentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CantNotFixContentException(Op op, String contentName, int v) {
        super(String.format("content is not fit for op: %s, %s, value:0x%x",
                op.displayName, contentName, v));
    }

    public CantNotFixContentException(Op op, String contentName, long v) {
        super(String.format("content is not fit for op: %s, %s, value:0x%x",
                op.displayName, contentName, v));
    }
}