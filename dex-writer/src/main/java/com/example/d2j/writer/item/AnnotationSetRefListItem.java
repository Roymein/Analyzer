package com.example.d2j.writer.item;

import com.example.d2j.writer.ann.Off;
import com.example.d2j.writer.io.DataOut;

public class AnnotationSetRefListItem extends BaseItem {
    @Off
    final public AnnotationSetItem[] annotationSets;

    public AnnotationSetRefListItem(int size) {
        this.annotationSets = new AnnotationSetItem[size];
    }

    @Override
    public int place(int offset) {
        return offset + 4 + annotationSets.length * 4;
    }

    @Override
    public void write(DataOut out) {
        out.uint("size", annotationSets.length);
        for (AnnotationSetItem item : annotationSets) {
            out.uint("annotations_off", item == null ? 0 : item.offset);
        }
    }
}
