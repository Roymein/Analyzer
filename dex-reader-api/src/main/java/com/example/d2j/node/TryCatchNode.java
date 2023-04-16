package com.example.d2j.node;

import com.example.d2j.DexLabel;
import com.example.d2j.visitors.DexCodeVisitor;

public class TryCatchNode {

    public final DexLabel start;
    public final DexLabel end;
    public final DexLabel[] handler;
    public final String[] type;

    public TryCatchNode(DexLabel start, DexLabel end, DexLabel[] handler, String[] type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

    public void accept(DexCodeVisitor cv) {
        cv.visitTryCatch(start, end, handler, type);
    }
}